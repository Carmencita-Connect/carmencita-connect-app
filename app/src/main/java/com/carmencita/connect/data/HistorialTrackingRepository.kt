package com.carmencita.connect.data

import android.content.Context
import org.json.JSONArray

class HistorialTrackingRepository(context: Context) {

    private val preferences = context.applicationContext.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    fun obtenerCodigos(): List<String> {
        val contenido = preferences.getString(KEY_CODIGOS, "[]").orEmpty()

        return runCatching {
            val array = JSONArray(contenido)
            buildList {
                for (index in 0 until array.length()) {
                    val codigo = array.optString(index).trim()
                    if (codigo.isNotBlank()) add(codigo)
                }
            }
        }.getOrDefault(emptyList())
    }

    fun guardarCodigo(numeroGuia: String): List<String> {
        val codigos = HistorialTrackingPolicy.agregar(obtenerCodigos(), numeroGuia)
        guardarLista(codigos)
        return codigos
    }

    fun limpiar() {
        preferences.edit().remove(KEY_CODIGOS).apply()
    }

    private fun guardarLista(codigos: List<String>) {
        val array = JSONArray()
        codigos.forEach(array::put)
        preferences.edit().putString(KEY_CODIGOS, array.toString()).apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "historial_tracking"
        const val KEY_CODIGOS = "codigos_consultados"
    }
}
