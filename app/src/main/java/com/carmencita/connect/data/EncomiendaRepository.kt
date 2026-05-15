package com.carmencita.connect.data

import com.carmencita.connect.model.Encomienda

class EncomiendaRepository {

    // Datos mock — se reemplaza por API real en sprint 2
    private val encomiendas = listOf(
        Encomienda(
            id = 1,
            numeroGuia = "C000000001",
            estado = "EN AGENCIA",
            remitente = "Sofia Suarez",
            destinatario = "Marta Sanchez",
            origen = "Trujillo",
            destino = "Angasmarca",
            peso = 10.0,
            fechaRegistro = "20/05/26",
            fechaTransito = "20/05/26",
            fechaAgencia = "21/05/26",
            fechaEntrega = null
        )
    )

    fun buscarPorGuia(numeroGuia: String): Encomienda? {
        return encomiendas.find {
            it.numeroGuia.equals(numeroGuia.trim(), ignoreCase = true)
        }
    }
}