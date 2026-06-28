package com.carmencita.connect.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface EncomiendaDao {

    @Query("SELECT * FROM encomiendas WHERE UPPER(numeroGuia) = UPPER(:numeroGuia) LIMIT 1")
    fun obtenerPorNumeroGuia(numeroGuia: String): EncomiendaEntity?
}
