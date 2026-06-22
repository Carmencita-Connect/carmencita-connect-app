package com.carmencita.connect.data

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import com.carmencita.connect.data.local.toEntity
import com.carmencita.connect.data.security.PasswordHasher
import com.carmencita.connect.model.Persona

class AuthRepository(context: Context) {

    private val database = AppDatabase.obtener(context)
    private val personaDao = database.personaDao()
    private val sesionRepository = SesionRepository(context)

    data class AuthResult(
        val exitoso: Boolean,
        val mensaje: String = "",
        val persona: Persona? = null
    )

    fun validarDisponibilidad(correo: String, dni: String): AuthResult {
        val correoNormalizado = correo.trim().lowercase()
        if (personaDao.obtenerPorCorreo(correoNormalizado) != null) {
            return AuthResult(false, "El correo ya está registrado")
        }
        if (personaDao.obtenerPorDni(dni.trim()) != null) {
            return AuthResult(false, "El DNI ya está registrado")
        }
        return AuthResult(true)
    }

    fun registrarConfirmado(persona: Persona, password: String): AuthResult {
        val disponibilidad = validarDisponibilidad(persona.correo, persona.dni)
        if (!disponibilidad.exitoso) return disponibilidad

        return try {
            val salt = PasswordHasher.generarSalt()
            val hash = PasswordHasher.hash(password, salt)
            val personaId = personaDao.insertar(
                persona.toEntity(passwordHash = hash, salt = salt)
            )
            sesionRepository.guardarSesion(personaId, persona.correo.trim().lowercase())
            AuthResult(exitoso = true, persona = persona)
        } catch (ex: SQLiteConstraintException) {
            AuthResult(false, "El correo o DNI ya está registrado")
        } catch (ex: Exception) {
            AuthResult(false, "No se pudo registrar el usuario")
        }
    }

    fun iniciarSesion(correo: String, password: String): AuthResult {
        val correoNormalizado = correo.trim().lowercase()
        val persona = personaDao.obtenerPorCorreo(correoNormalizado)
            ?: return AuthResult(false, "Correo o contraseña incorrectos")

        if (!persona.correoVerificado ||
            !PasswordHasher.verificar(password, persona.salt, persona.passwordHash)
        ) {
            return AuthResult(false, "Correo o contraseña incorrectos")
        }

        sesionRepository.guardarSesion(persona.id, correoNormalizado)
        return AuthResult(exitoso = true, persona = persona.toModel())
    }

    fun cerrarSesion() {
        sesionRepository.cerrarSesionLocal()
    }
}
