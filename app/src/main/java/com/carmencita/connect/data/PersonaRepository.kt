package com.carmencita.connect.data

import android.content.Context
import com.carmencita.connect.model.Persona

class PersonaRepository(context: Context) {

    private val database = AppDatabase.obtener(context)
    private val personaDao = database.personaDao()
    private val sesionRepository = SesionRepository(context)

    data class PersonaResult(
        val exitoso: Boolean,
        val mensaje: String = "",
        val persona: Persona? = null
    )

    fun obtenerPersonaActual(): PersonaResult {
        val personaId = sesionRepository.obtenerPersonaId()
            ?: return PersonaResult(false, "No hay una sesion activa")

        val persona = personaDao.obtenerPorId(personaId)
            ?: return PersonaResult(false, "No se encontro el perfil del usuario")

        return PersonaResult(
            exitoso = true,
            persona = persona.toModel()
        )
    }

    fun actualizarTelefono(telefono: String): PersonaResult {
        val personaActual = obtenerPersonaActual()
        if (!personaActual.exitoso || personaActual.persona == null) return personaActual

        return actualizarPerfil(personaActual.persona.nombre, telefono)
    }

    fun actualizarPerfil(nombre: String, telefono: String): PersonaResult {
        val personaId = sesionRepository.obtenerPersonaId()
            ?: return PersonaResult(false, "No hay una sesion activa")

        val filas = personaDao.actualizarPerfil(personaId, nombre.trim(), telefono.trim())
        if (filas == 0) {
            return PersonaResult(false, "No se pudo actualizar el perfil")
        }

        val personaActualizada = personaDao.obtenerPorId(personaId)
            ?: return PersonaResult(false, "No se encontro el perfil del usuario")

        return PersonaResult(
            exitoso = true,
            mensaje = "Datos actualizados correctamente",
            persona = personaActualizada.toModel()
        )
    }
}
