package com.carmencita.connect.model

data class Comprobante(
    val id: Int = 0,
    val numeroPR: String = "",     // referencia al PreRegistro
    val remitente: String = "",
    val destinatario: String = "",
    val origen: String = "",
    val destino: String = "",
    val peso: Double = 0.0,
    val metodoPago: String = "",
    val total: Double = 0.0,
    val fechaGeneracion: String = ""
)
