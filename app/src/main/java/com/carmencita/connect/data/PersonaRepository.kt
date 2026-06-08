package com.carmencita.connect.data

import android.content.Context
import com.carmencita.connect.model.Persona
import org.json.JSONObject

class PersonaRepository(context: Context) {

    private val sesionRepository = SesionRepository(context)

    data class PersonaResult(
        val exitoso: Boolean,
        val mensaje: String = "",
        val persona: Persona? = null
    )

    fun obtenerPersonaActual(): PersonaResult {
        val token = sesionRepository.obtenerToken()
        if (token.isBlank()) {
            return PersonaResult(false, "No hay una sesión activa")
        }

        val response = ApiClient.get("/api/personas/me", token)
        if (!response.isSuccessful) {
            return PersonaResult(
                exitoso = false,
                mensaje = ApiJsonMapper.errorMessage(
                    response.body,
                    "No se pudo cargar el perfil"
                )
            )
        }

        val json = JSONObject(response.body)
        return PersonaResult(
            exitoso = true,
            persona = ApiJsonMapper.personaFromJson(json.getJSONObject("persona"))
        )
    }

    fun actualizarTelefono(telefono: String): PersonaResult {
        val token = sesionRepository.obtenerToken()
        if (token.isBlank()) {
            return PersonaResult(false, "No hay una sesión activa")
        }

        val body = JSONObject()
            .put("telefono", telefono)
            .toString()

        val response = ApiClient.patch("/api/personas/me", body, token)
        if (!response.isSuccessful) {
            return PersonaResult(
                exitoso = false,
                mensaje = ApiJsonMapper.errorMessage(
                    response.body,
                    "No se pudo actualizar el perfil"
                )
            )
        }

        val json = JSONObject(response.body)
        return PersonaResult(
            exitoso = true,
            mensaje = "Datos actualizados correctamente",
            persona = ApiJsonMapper.personaFromJson(json.getJSONObject("persona"))
        )
    }
}
