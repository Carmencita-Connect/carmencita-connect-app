package com.carmencita.connect.data

import android.content.Context

class SesionRepository(context: Context) {

    private val preferences = context.applicationContext.getSharedPreferences(
        "carmencita_sesion",
        Context.MODE_PRIVATE
    )

    fun guardarSesion(personaId: Long, correo: String) {
        preferences.edit()
            .putLong(KEY_PERSONA_ID, personaId)
            .putString(KEY_CORREO, correo)
            .putBoolean(KEY_ACTIVA, true)
            .apply()
    }

    fun obtenerPersonaId(): Long? {
        if (!haySesionActiva()) return null
        return preferences.getLong(KEY_PERSONA_ID, SIN_PERSONA).takeIf { it != SIN_PERSONA }
    }

    fun haySesionActiva(): Boolean {
        return preferences.getBoolean(KEY_ACTIVA, false) &&
            preferences.getLong(KEY_PERSONA_ID, SIN_PERSONA) != SIN_PERSONA
    }

    fun cerrarSesionLocal() {
        preferences.edit().clear().apply()
    }

    private companion object {
        const val KEY_PERSONA_ID = "persona_id"
        const val KEY_CORREO = "correo"
        const val KEY_ACTIVA = "activa"
        const val SIN_PERSONA = -1L
    }
}
