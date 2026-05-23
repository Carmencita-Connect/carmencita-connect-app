package com.carmencita.connect.data

import com.carmencita.connect.model.Tarifa

class TarifaRepository {

    fun calcularTarifa(
        largo: Double,
        ancho: Double,
        alto: Double,
        peso: Double,
        destino: String
    ): Tarifa {
        val volumen = largo * ancho * alto
        val tarifaBase = when (destino) {
            "Angasmarca"        -> 20.0
            "Santiago de Chuco" -> 15.0
            else                -> 10.0
        }
        val costoPeso    = peso * 2.5
        val costoVolumen = volumen * 0.001
        val total = tarifaBase + costoPeso + costoVolumen

        return Tarifa(
            destino = destino,
            costo   = total
        )
    }
}