package com.carmencita.connect.data

import android.content.Context
import com.carmencita.connect.model.Encomienda
import com.carmencita.connect.model.Tarifa

class EncomiendaRepository private constructor(
    private val database: AppDatabase?
) {

    constructor() : this(null)

    constructor(context: Context) : this(AppDatabase.obtener(context))

    fun crearCotizada(
        largo: Double,
        ancho: Double,
        alto: Double,
        peso: Double,
        origen: String,
        destino: String,
        tarifa: Tarifa
    ): Encomienda {
        return Encomienda(
            largo = largo,
            ancho = ancho,
            alto = alto,
            peso = peso,
            origen = origen,
            destino = destino,
            tarifa = tarifa,
            estado = "pendiente"
        )
    }

    fun buscarPorGuia(numeroGuia: String): Encomienda? {
        val encomiendaDao = requireNotNull(database) {
            "Se requiere Context para consultar encomiendas persistidas"
        }.encomiendaDao()

        return encomiendaDao.obtenerPorNumeroGuia(numeroGuia.trim())?.toModel()
    }
}
