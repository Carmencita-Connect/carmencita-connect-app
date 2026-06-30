package com.carmencita.connect.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.carmencita.connect.model.Persona

@Entity(
    tableName = "contactos_frecuentes",
    indices = [Index(value = ["personaId", "dni"], unique = true)]
)
data class ContactoFrecuenteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val personaId: Long,
    val nombre: String,
    val dni: String,
    val telefono: String,
    val direccion: String
) {
    fun toPersona(): Persona {
        return Persona(
            nombre = nombre,
            dni = dni,
            telefono = telefono,
            direccion = direccion
        )
    }
}
