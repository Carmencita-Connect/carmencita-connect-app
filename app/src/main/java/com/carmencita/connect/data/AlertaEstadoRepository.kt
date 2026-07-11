package com.carmencita.connect.data

import android.content.Context
import com.carmencita.connect.model.Alerta

class AlertaEstadoRepository(context: Context) {

    private val preferences = context.applicationContext.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )
    private val sesionRepository = SesionRepository(context)

    fun alertasActivas(): Boolean {
        val personaId = sesionRepository.obtenerPersonaId() ?: return false
        return preferences.getBoolean(keyAlertasActivas(personaId), false)
    }

    fun configurarAlertas(activo: Boolean) {
        val personaId = sesionRepository.obtenerPersonaId() ?: return
        preferences.edit().putBoolean(keyAlertasActivas(personaId), activo).apply()
    }

    fun estadoActual(): String {
        val personaId = sesionRepository.obtenerPersonaId() ?: return AlertaEstadoPolicy.ESTADO_REGISTRADO
        return preferences.getString(
            keyEstadoActual(personaId),
            AlertaEstadoPolicy.ESTADO_REGISTRADO
        ).orEmpty().ifBlank { AlertaEstadoPolicy.ESTADO_REGISTRADO }
    }

    fun simularCambioEstado(): Alerta? {
        val personaId = sesionRepository.obtenerPersonaId() ?: return null
        val anterior = estadoActual()
        val nuevo = AlertaEstadoPolicy.siguienteEstado(anterior)
        preferences.edit().putString(keyEstadoActual(personaId), nuevo).apply()

        return Alerta(
            numeroGuia = NUMERO_GUIA_DEMO,
            estadoAnterior = anterior,
            estadoNuevo = nuevo,
            mensaje = AlertaEstadoPolicy.mensajeParaEstado(NUMERO_GUIA_DEMO, nuevo),
            requiereRecojo = nuevo == AlertaEstadoPolicy.ESTADO_AGENCIA
        )
    }

    private fun keyAlertasActivas(personaId: Long) = "alertas_activas_$personaId"

    private fun keyEstadoActual(personaId: Long) = "estado_actual_$personaId"

    private companion object {
        const val PREFERENCES_NAME = "alertas_estado"
        const val NUMERO_GUIA_DEMO = "C000000001"
    }
}
