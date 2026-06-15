package com.carmencita.connect.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CredencialDao {

    @Insert
    fun insertar(credencial: CredencialEntity): Long

    @Query("SELECT * FROM credenciales WHERE correo = :correo LIMIT 1")
    fun obtenerPorCorreo(correo: String): CredencialEntity?
}
