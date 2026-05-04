package com.carmencita.connect.model

data class PreRegistro(
    val id: Int = 0,
    val remitente: String = "",
    val destinatario: String = "",
    val destino: String = "",
    val peso: String = "",
    val alto: Double = 0.0,
    val largo: Double = 0.0,
    val ancho: Double = 0.0,
    val metodoPago: String = "",
    val estado: String = "pendiente"
)