package com.carmencita.connect.data

import com.carmencita.connect.model.Persona
import org.json.JSONObject

object ApiJsonMapper {

    fun personaFromJson(json: JSONObject): Persona {
        return Persona(
            nombre = json.optString("nombre"),
            dni = json.optString("dni"),
            telefono = json.optString("telefono"),
            direccion = json.optString("direccion")
        )
    }

    fun errorMessage(body: String, fallback: String): String {
        return runCatching {
            JSONObject(body).optString("message").ifBlank { fallback }
        }.getOrDefault(fallback)
    }
}
