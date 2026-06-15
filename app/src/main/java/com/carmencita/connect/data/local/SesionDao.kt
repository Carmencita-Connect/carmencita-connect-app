package com.carmencita.connect.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SesionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun guardar(sesion: SesionEntity)

    @Query("SELECT * FROM sesiones WHERE id = 1 AND activa = 1 LIMIT 1")
    fun obtenerActiva(): SesionEntity?

    @Query("DELETE FROM sesiones")
    fun cerrarTodas()
}
