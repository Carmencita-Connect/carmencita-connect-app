package com.carmencita.connect.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.carmencita.connect.model.Sesion

@Entity(
    tableName = "sesiones",
    foreignKeys = [
        ForeignKey(
            entity = PersonaEntity::class,
            parentColumns = ["id"],
            childColumns = ["personaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["personaId"])]
)
data class SesionEntity(
    @PrimaryKey val id: Int = SESION_ACTIVA_ID,
    val personaId: Long,
    val correo: String,
    val activa: Boolean = true
) {
    fun toModel(): Sesion {
        return Sesion(
            token = personaId.toString(),
            correo = correo,
            activa = activa
        )
    }

    companion object {
        const val SESION_ACTIVA_ID = 1
    }
}
