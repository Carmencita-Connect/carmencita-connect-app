package com.carmencita.connect.data

import android.content.Context
import com.carmencita.connect.model.Sesion

class SesionRepository(context: Context) {

    private val preferences = context.applicationContext.getSharedPreferences(
        "carmencita_sesion",
        Context.MODE_PRIVATE
    )

    fun guardarSesion(sesion: Sesion) {
        preferences.edit()
            .putString(KEY_TOKEN, sesion.token)
            .putString(KEY_CORREO, sesion.correo)
            .putBoolean(KEY_ACTIVA, sesion.activa)
            .apply()
    }

    fun obtenerSesion(): Sesion {
        return Sesion(
            token = preferences.getString(KEY_TOKEN, "").orEmpty(),
            correo = preferences.getString(KEY_CORREO, "").orEmpty(),
            activa = preferences.getBoolean(KEY_ACTIVA, false)
        )
    }

    fun obtenerToken(): String = obtenerSesion().token

    fun haySesionActiva(): Boolean {
        val sesion = obtenerSesion()
        return sesion.activa && sesion.token.isNotBlank()
    }

    fun cerrarSesionLocal() {
        preferences.edit().clear().apply()
    }

    private companion object {
        const val KEY_TOKEN = "token"
        const val KEY_CORREO = "correo"
        const val KEY_ACTIVA = "activa"
    }
}
