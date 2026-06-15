package com.carmencita.connect.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PersonaDao {

    @Insert
    fun insertar(persona: PersonaEntity): Long

    @Query("SELECT * FROM personas WHERE id = :id LIMIT 1")
    fun obtenerPorId(id: Long): PersonaEntity?

    @Query("SELECT * FROM personas WHERE dni = :dni LIMIT 1")
    fun obtenerPorDni(dni: String): PersonaEntity?

    @Query("UPDATE personas SET telefono = :telefono WHERE id = :id")
    fun actualizarTelefono(id: Long, telefono: String): Int
}
