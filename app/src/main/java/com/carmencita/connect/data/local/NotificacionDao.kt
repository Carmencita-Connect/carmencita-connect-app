package com.carmencita.connect.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotificacionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertar(notificacion: NotificacionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarTodas(notificaciones: List<NotificacionEntity>)

    @Query(
        """
        SELECT * FROM notificaciones
        WHERE personaId = :personaId
        ORDER BY creadoEn DESC
        """
    )
    fun listarPorPersona(personaId: Long): List<NotificacionEntity>

    @Query("SELECT COUNT(*) FROM notificaciones WHERE personaId = :personaId")
    fun contarPorPersona(personaId: Long): Int

    @Query("DELETE FROM notificaciones WHERE personaId = :personaId")
    fun limpiarPorPersona(personaId: Long)

    @Query(
        """
        DELETE FROM notificaciones
        WHERE personaId = :personaId
        AND id NOT IN (
            SELECT id FROM notificaciones
            WHERE personaId = :personaId
            ORDER BY creadoEn DESC
            LIMIT :limite
        )
        """
    )
    fun eliminarExcedentes(personaId: Long, limite: Int)
}
