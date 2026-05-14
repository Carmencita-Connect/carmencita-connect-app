package com.carmencita.connect.data

class TarifaRepository {

    fun calcularTarifa(
        largo: Double,
        ancho: Double,
        alto: Double,
        peso: Double,
        destino: String
    ): Double {
        val volumen = largo * ancho * alto
        val tarifaBase = when (destino) {
            "Angasmarca"        -> 20.0
            "Santiago de Chuco" -> 15.0
            else                -> 10.0  // Trujillo
        }
        val costoPeso    = peso * 2.5
        val costoVolumen = volumen * 0.001
        return tarifaBase + costoPeso + costoVolumen
    }
}