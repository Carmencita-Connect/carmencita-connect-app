package com.carmencita.connect.data

import com.carmencita.connect.model.Notificacion

object NotificacionHistoryPolicy {

    const val LIMITE_NOTIFICACIONES = 20

    fun agregar(
        notificacionesActuales: List<Notificacion>,
        nuevaNotificacion: Notificacion
    ): List<Notificacion> {
        if (nuevaNotificacion.titulo.isBlank() || nuevaNotificacion.mensaje.isBlank()) {
            return notificacionesActuales
        }

        return buildList {
            add(nuevaNotificacion)
            addAll(notificacionesActuales.filterNot { it.id == nuevaNotificacion.id })
        }.take(LIMITE_NOTIFICACIONES)
    }
}
