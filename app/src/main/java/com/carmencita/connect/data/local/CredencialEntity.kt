package com.carmencita.connect.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "credenciales",
    foreignKeys = [
        ForeignKey(
            entity = PersonaEntity::class,
            parentColumns = ["id"],
            childColumns = ["personaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["correo"], unique = true),
        Index(value = ["personaId"])
    ]
)
data class CredencialEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val personaId: Long,
    val correo: String,
    val passwordHash: String,
    val salt: String
)
