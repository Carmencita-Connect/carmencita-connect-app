package com.carmencita.connect.data

import android.content.Context
import com.carmencita.connect.model.Persona
import com.carmencita.connect.model.Sesion
import org.json.JSONObject

class AuthRepository(context: Context) {

    private val sesionRepository = SesionRepository(context)

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
        val body = JSONObject()
            .put("nombre", persona.nombre)
            .put("dni", persona.dni)
            .put("telefono", persona.telefono)
            .put("direccion", persona.direccion)
            .put("correo", correo)
            .put("password", password)
            .toString()

        return ejecutarAutenticacion(
            response = ApiClient.post("/api/auth/register", body),
            correo = correo
        )
    }

    fun iniciarSesion(correo: String, password: String): AuthResult {
        val body = JSONObject()
            .put("correo", correo)
            .put("password", password)
            .toString()

        return ejecutarAutenticacion(
            response = ApiClient.post("/api/auth/login", body),
            correo = correo
        )
    }

    fun cerrarSesion() {
        val token = sesionRepository.obtenerToken()
        if (token.isNotBlank()) {
            runCatching {
                ApiClient.post("/api/auth/logout", "{}", token)
            }
        }
        sesionRepository.cerrarSesionLocal()
    }

    private fun ejecutarAutenticacion(
        response: ApiClient.ApiResponse,
        correo: String
    ): AuthResult {
        if (!response.isSuccessful) {
            return AuthResult(
                exitoso = false,
                mensaje = ApiJsonMapper.errorMessage(
                    response.body,
                    "No se pudo completar la operación"
                )
            )
        }

        val json = JSONObject(response.body)
        val token = json.optString("token")
        val persona = ApiJsonMapper.personaFromJson(json.getJSONObject("persona"))
        val sesion = Sesion(
            token = token,
            correo = correo,
            activa = token.isNotBlank()
        )

        sesionRepository.guardarSesion(sesion)
        return AuthResult(
            exitoso = true,
            persona = persona,
            sesion = sesion
        )
    }
}
