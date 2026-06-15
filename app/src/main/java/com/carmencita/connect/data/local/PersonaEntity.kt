package com.carmencita.connect.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.carmencita.connect.model.Persona

@Entity(
    tableName = "personas",
    indices = [Index(value = ["dni"], unique = true)]
)
data class PersonaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val dni: String,
    val telefono: String,
    val direccion: String = ""
) {
    fun toModel(): Persona {
        return Persona(
            nombre = nombre,
            dni = dni,
            telefono = telefono,
            direccion = direccion
        )
    }
}

fun Persona.toEntity(id: Long = 0): PersonaEntity {
    return PersonaEntity(
        id = id,
        nombre = nombre,
        dni = dni,
        telefono = telefono,
        direccion = direccion
    )
}
