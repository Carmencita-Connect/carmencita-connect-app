package com.carmencita.connect.model

data class Comprobante(
    val id: Int = 0,
    val pago: Pago = Pago(),
    val total: Double = 0.0,
    val fechaGeneracion: String = ""
)