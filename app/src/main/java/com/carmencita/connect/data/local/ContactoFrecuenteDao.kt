package com.carmencita.connect.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ContactoFrecuenteDao {

    @Insert
    fun insertar(contacto: ContactoFrecuenteEntity): Long

    @Query(
        """
        SELECT * FROM contactos_frecuentes
        WHERE personaId = :personaId
        ORDER BY nombre COLLATE NOCASE ASC
        """
    )
    fun listarPorPersona(personaId: Long): List<ContactoFrecuenteEntity>

    @Query(
        """
        SELECT * FROM contactos_frecuentes
        WHERE personaId = :personaId
        AND (
            nombre LIKE '%' || :texto || '%'
            OR dni LIKE '%' || :texto || '%'
        )
        ORDER BY nombre COLLATE NOCASE ASC
        """
    )
    fun buscar(personaId: Long, texto: String): List<ContactoFrecuenteEntity>

    @Query("SELECT * FROM contactos_frecuentes WHERE dni = :dni AND personaId = :personaId LIMIT 1")
    fun obtenerPorDni(dni: String, personaId: Long): ContactoFrecuenteEntity?

    @Query(
        """
        UPDATE contactos_frecuentes
        SET telefono = :telefono
        WHERE dni = :dni AND personaId = :personaId
        """
    )
    fun actualizarTelefono(dni: String, personaId: Long, telefono: String): Int

    @Query("DELETE FROM contactos_frecuentes WHERE dni = :dni AND personaId = :personaId")
    fun eliminar(dni: String, personaId: Long): Int
}
