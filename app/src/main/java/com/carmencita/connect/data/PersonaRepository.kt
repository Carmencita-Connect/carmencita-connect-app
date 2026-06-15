package com.carmencita.connect.data

import android.content.Context
import com.carmencita.connect.model.Persona

class PersonaRepository(context: Context) {

    private val database = AppDatabase.obtener(context)
    private val personaDao = database.personaDao()
    private val sesionDao = database.sesionDao()

    data class PersonaResult(
        val exitoso: Boolean,
        val mensaje: String = "",
        val persona: Persona? = null
    )

    fun obtenerPersonaActual(): PersonaResult {
        val sesion = sesionDao.obtenerActiva()
            ?: return PersonaResult(false, "No hay una sesión activa")

        val persona = personaDao.obtenerPorId(sesion.personaId)
            ?: return PersonaResult(false, "No se encontró el perfil del usuario")

        return PersonaResult(
            exitoso = true,
            persona = persona.toModel()
        )
    }

    fun actualizarTelefono(telefono: String): PersonaResult {
        val sesion = sesionDao.obtenerActiva()
            ?: return PersonaResult(false, "No hay una sesión activa")

        val filas = personaDao.actualizarTelefono(sesion.personaId, telefono)
        if (filas == 0) {
            return PersonaResult(false, "No se pudo actualizar el perfil")
        }

        val personaActualizada = personaDao.obtenerPorId(sesion.personaId)
            ?: return PersonaResult(false, "No se encontró el perfil del usuario")

        return PersonaResult(
            exitoso = true,
            mensaje = "Datos actualizados correctamente",
            persona = personaActualizada.toModel()
        )
    }
}
