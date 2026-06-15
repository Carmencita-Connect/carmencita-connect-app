package com.carmencita.connect.data

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import com.carmencita.connect.data.local.CredencialEntity
import com.carmencita.connect.data.local.SesionEntity
import com.carmencita.connect.data.local.toEntity
import com.carmencita.connect.data.security.PasswordHasher
import com.carmencita.connect.model.Persona
import com.carmencita.connect.model.Sesion

class AuthRepository(context: Context) {

    private val database = AppDatabase.obtener(context)
    private val personaDao = database.personaDao()
    private val credencialDao = database.credencialDao()
    private val sesionDao = database.sesionDao()

    data class AuthResult(
        val exitoso: Boolean,
        val mensaje: String = "",
        val persona: Persona? = null,
        val sesion: Sesion? = null
    )

    fun registrar(
        persona: Persona,
        correo: String,
        password: String
    ): AuthResult {
        val correoNormalizado = correo.trim().lowercase()
        if (credencialDao.obtenerPorCorreo(correoNormalizado) != null) {
            return AuthResult(false, "El correo ya está registrado")
        }
        if (personaDao.obtenerPorDni(persona.dni.trim()) != null) {
            return AuthResult(false, "El DNI ya está registrado")
        }

        return try {
            var personaId = 0L
            val salt = PasswordHasher.generarSalt()
            val hash = PasswordHasher.hash(password, salt)
            database.runInTransaction {
                personaId = personaDao.insertar(persona.toEntity())
                credencialDao.insertar(
                    CredencialEntity(
                        personaId = personaId,
                        correo = correoNormalizado,
                        passwordHash = hash,
                        salt = salt
                    )
                )
                sesionDao.cerrarTodas()
                sesionDao.guardar(
                    SesionEntity(
                        personaId = personaId,
                        correo = correoNormalizado,
                        activa = true
                    )
                )
            }
            AuthResult(
                exitoso = true,
                persona = persona,
                sesion = Sesion(
                    token = personaId.toString(),
                    correo = correoNormalizado,
                    activa = true
                )
            )
        } catch (ex: SQLiteConstraintException) {
            AuthResult(false, "El correo o DNI ya está registrado")
        } catch (ex: Exception) {
            AuthResult(false, "No se pudo registrar el usuario")
        }
    }

    fun iniciarSesion(correo: String, password: String): AuthResult {
        val correoNormalizado = correo.trim().lowercase()
        val credencial = credencialDao.obtenerPorCorreo(correoNormalizado)
            ?: return AuthResult(false, "Correo o contraseña incorrectos")

        if (!PasswordHasher.verificar(password, credencial.salt, credencial.passwordHash)) {
            return AuthResult(false, "Correo o contraseña incorrectos")
        }

        val persona = personaDao.obtenerPorId(credencial.personaId)
            ?: return AuthResult(false, "No se encontró el perfil del usuario")

        sesionDao.cerrarTodas()
        sesionDao.guardar(
            SesionEntity(
                personaId = credencial.personaId,
                correo = correoNormalizado,
                activa = true
            )
        )

        return AuthResult(
            exitoso = true,
            persona = persona.toModel(),
            sesion = Sesion(
                token = credencial.personaId.toString(),
                correo = correoNormalizado,
                activa = true
            )
        )
    }

    fun cerrarSesion() {
        sesionDao.cerrarTodas()
    }
}
