package com.carmencita.connect.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.carmencita.connect.model.Notificacion

@Entity(
    tableName = "notificaciones",
    indices = [Index(value = ["personaId", "creadoEn"])]
)
data class NotificacionEntity(
    @PrimaryKey val id: String,
    val personaId: Long,
    val titulo: String,
    val mensaje: String,
    val fecha: String,
    val estado: String,
    val creadoEn: Long
) {
    fun toModel(): Notificacion {
        return Notificacion(
            id = id,
            titulo = titulo,
            mensaje = mensaje,
            fecha = fecha,
            estado = estado
        )
    }
}
