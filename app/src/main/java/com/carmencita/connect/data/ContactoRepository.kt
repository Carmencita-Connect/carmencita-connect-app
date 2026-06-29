package com.carmencita.connect.data

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import com.carmencita.connect.data.local.ContactoFrecuenteEntity
import com.carmencita.connect.model.Persona

class ContactoRepository(context: Context) {

    private val contactoDao = AppDatabase.obtener(context).contactoFrecuenteDao()
    private val sesionRepository = SesionRepository(context)

    data class ContactoResult(
        val exitoso: Boolean,
        val mensaje: String = "",
        val contactos: List<Persona> = emptyList(),
        val contacto: Persona? = null
    )

    fun listar(): ContactoResult {
        val personaId = obtenerPersonaIdActual()
            ?: return ContactoResult(false, "No hay una sesion activa")

        return ContactoResult(
            exitoso = true,
            contactos = contactoDao.listarPorPersona(personaId).map { it.toPersona() }
        )
    }

    fun buscar(texto: String): ContactoResult {
        val personaId = obtenerPersonaIdActual()
            ?: return ContactoResult(false, "No hay una sesion activa")

        val contactos = if (texto.isBlank()) {
            contactoDao.listarPorPersona(personaId)
        } else {
            contactoDao.buscar(personaId, texto.trim())
        }

        return ContactoResult(
            exitoso = true,
            contactos = contactos.map { it.toPersona() }
        )
    }

    fun guardar(nombre: String, dni: String, telefono: String, direccion: String): ContactoResult {
        val personaId = obtenerPersonaIdActual()
            ?: return ContactoResult(false, "No hay una sesion activa")

        val validacion = validar(nombre, dni, telefono, direccion)
        if (validacion.isNotBlank()) return ContactoResult(false, validacion)

        val contacto = Persona(
            nombre = nombre.trim(),
            dni = dni.trim(),
            telefono = telefono.trim(),
            direccion = direccion.trim()
        )

        return try {
            contactoDao.insertar(
                ContactoFrecuenteEntity(
                    personaId = personaId,
                    nombre = contacto.nombre,
                    dni = contacto.dni,
                    telefono = contacto.telefono,
                    direccion = contacto.direccion
                )
            )
            ContactoResult(
                exitoso = true,
                mensaje = "Contacto guardado correctamente",
                contacto = contacto
            )
        } catch (ex: SQLiteConstraintException) {
            ContactoResult(false, "Ya existe un contacto con ese DNI")
        } catch (ex: Exception) {
            ContactoResult(false, "No se pudo guardar el contacto")
        }
    }

    fun actualizarTelefono(dni: String, telefono: String): ContactoResult {
        val personaId = obtenerPersonaIdActual()
            ?: return ContactoResult(false, "No hay una sesion activa")

        if (dni.isBlank()) return ContactoResult(false, "No se encontro el contacto")
        if (telefono.length != 9 || !telefono.all { it.isDigit() }) {
            return ContactoResult(false, "El telefono debe tener 9 digitos")
        }

        val filas = contactoDao.actualizarTelefono(dni.trim(), personaId, telefono.trim())
        if (filas == 0) return ContactoResult(false, "No se pudo actualizar el contacto")

        val contacto = contactoDao.obtenerPorDni(dni.trim(), personaId)?.toPersona()
            ?: return ContactoResult(false, "No se encontro el contacto")

        return ContactoResult(
            exitoso = true,
            mensaje = "Contacto actualizado correctamente",
            contacto = contacto
        )
    }

    fun eliminar(dni: String): ContactoResult {
        val personaId = obtenerPersonaIdActual()
            ?: return ContactoResult(false, "No hay una sesion activa")

        val filas = contactoDao.eliminar(dni.trim(), personaId)
        if (filas == 0) return ContactoResult(false, "No se pudo eliminar el contacto")

        return ContactoResult(true, "Contacto eliminado correctamente")
    }

    private fun obtenerPersonaIdActual(): Long? = sesionRepository.obtenerPersonaId()

    private fun validar(
        nombre: String,
        dni: String,
        telefono: String,
        direccion: String
    ): String {
        if (nombre.isBlank() || dni.isBlank() || telefono.isBlank() || direccion.isBlank()) {
            return "Completa todos los campos"
        }
        if (!nombre.trim().all { it.isLetter() || it.isWhitespace() }) {
            return "El nombre solo debe contener letras"
        }
        if (dni.length != 8 || !dni.all { it.isDigit() }) {
            return "El DNI debe tener 8 digitos"
        }
        if (telefono.length != 9 || !telefono.all { it.isDigit() }) {
            return "El telefono debe tener 9 digitos"
        }
        return ""
    }
}
