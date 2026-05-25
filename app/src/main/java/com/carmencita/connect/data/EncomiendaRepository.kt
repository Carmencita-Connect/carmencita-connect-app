package com.carmencita.connect.data

import com.carmencita.connect.model.Encomienda
import com.carmencita.connect.model.Tarifa

class EncomiendaRepository {

    private val encomiendas = listOf(
        Encomienda(
            id           = 1,
            numeroGuia   = "C000000001",
            largo        = 20.0,
            ancho        = 20.0,
            alto         = 20.0,
            peso         = 10.0,
            origen       = "Trujillo",
            destino      = "Angasmarca",
            tarifa       = Tarifa(destino = "Angasmarca", costo = 45.0),
            estado       = "EN AGENCIA",
            fechaRegistro = "20/05/26",
            fechaTransito = "20/05/26",
            fechaAgencia  = "21/05/26",
            fechaEntrega  = null
        )
    )

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
        return encomiendas.find {
            it.numeroGuia.equals(numeroGuia.trim(), ignoreCase = true)
        }
    }
}
