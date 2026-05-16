package com.carmencita.connect.model

data class PreRegistro(
    val id: Int = 0,
    val remitente: String = "",
    val destinatario: String = "",
    val destino: String = "",
    val alto: Double = 0.0,
    val largo: Double = 0.0,
    val ancho: Double = 0.0,
    val peso: Double = 0.0,
    val metodoPago: String = "",
    val costoEstimado: Double = 0.0,
    val estado: String = "pendiente",
    val fechaCreacion: String = ""
)