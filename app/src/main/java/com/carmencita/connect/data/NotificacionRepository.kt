package com.carmencita.connect.data

import android.content.Context
import com.carmencita.connect.data.local.NotificacionEntity
import com.carmencita.connect.model.Notificacion
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class NotificacionRepository(context: Context) {

    private val notificacionDao = AppDatabase.obtener(context).notificacionDao()
    private val sesionRepository = SesionRepository(context)
    private val preferences = context.applicationContext.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    fun obtenerNotificaciones(): List<Notificacion> {
        val personaId = sesionRepository.obtenerPersonaId() ?: return emptyList()
        inicializarDatosDemoSiCorresponde(personaId)
        return notificacionDao.listarPorPersona(personaId).map { it.toModel() }
    }

    fun registrar(titulo: String, mensaje: String, estado: String): List<Notificacion> {
        val personaId = sesionRepository.obtenerPersonaId() ?: return emptyList()
        val creadoEn = System.currentTimeMillis()
        notificacionDao.insertar(
            NotificacionEntity(
                id = UUID.randomUUID().toString(),
                personaId = personaId,
                titulo = titulo.trim(),
                mensaje = mensaje.trim(),
                fecha = FORMATO_FECHA.format(Date(creadoEn)),
                estado = estado.trim(),
                creadoEn = creadoEn
            )
        )
        notificacionDao.eliminarExcedentes(
            personaId,
            NotificacionHistoryPolicy.LIMITE_NOTIFICACIONES
        )
        return notificacionDao.listarPorPersona(personaId).map { it.toModel() }
    }

    fun limpiar() {
        val personaId = sesionRepository.obtenerPersonaId() ?: return
        notificacionDao.limpiarPorPersona(personaId)
        marcarDatosDemoInicializados(personaId)
    }

    private fun inicializarDatosDemoSiCorresponde(personaId: Long) {
        if (datosDemoInicializados(personaId)) return
        if (notificacionDao.contarPorPersona(personaId) > 0) {
            marcarDatosDemoInicializados(personaId)
            return
        }

        notificacionDao.insertarTodas(notificacionesDemo(personaId))
        marcarDatosDemoInicializados(personaId)
    }

    private fun notificacionesDemo(personaId: Long): List<NotificacionEntity> = listOf(
        NotificacionEntity(
            id = "demo-$personaId-1",
            personaId = personaId,
            titulo = "Paquete listo para recojo",
            mensaje = "Tu encomienda C000000001 se encuentra disponible en agencia.",
            fecha = "21/06/2026 09:30",
            estado = "EN AGENCIA",
            creadoEn = 1_787_302_200_000
        ),
        NotificacionEntity(
            id = "demo-$personaId-2",
            personaId = personaId,
            titulo = "Cambio de estado",
            mensaje = "Tu encomienda C000000345 paso a estado En transito.",
            fecha = "19/06/2026 15:45",
            estado = "EN TRANSITO",
            creadoEn = 1_787_156_700_000
        ),
        NotificacionEntity(
            id = "demo-$personaId-3",
            personaId = personaId,
            titulo = "Envio registrado",
            mensaje = "Tu encomienda C000000782 fue registrada correctamente.",
            fecha = "21/06/2026 08:10",
            estado = "REGISTRADO",
            creadoEn = 1_787_297_400_000
        )
    )

    private fun datosDemoInicializados(personaId: Long): Boolean {
        return preferences.getBoolean(keyDatosInicializados(personaId), false)
    }

    private fun marcarDatosDemoInicializados(personaId: Long) {
        preferences.edit().putBoolean(keyDatosInicializados(personaId), true).apply()
    }

    private fun keyDatosInicializados(personaId: Long) =
        "${KEY_DATOS_INICIALIZADOS}_$personaId"

    private companion object {
        const val PREFERENCES_NAME = "historial_notificaciones"
        const val KEY_DATOS_INICIALIZADOS = "datos_inicializados"
        val FORMATO_FECHA = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    }
}
