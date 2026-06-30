package com.carmencita.connect.model

data class Encomienda(
    val id: Int = 0,
    val numeroGuia: String = "",
    val largo: Double = 0.0,
    val ancho: Double = 0.0,
    val alto: Double = 0.0,
    val peso: Double = 0.0,
    val origen: String = "",
    val destino: String = "",
    val tarifa: Tarifa = Tarifa(),
    val estado: String = "",
    val fechaRegistro: String? = null,
    val fechaTransito: String? = null,
    val fechaAgencia: String? = null,
    val fechaEntrega: String? = null
)