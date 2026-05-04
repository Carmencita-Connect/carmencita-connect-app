package com.carmencita.connect.model

data class Encomienda(
    val id: Int = 0,
    val numeroGuia: String = "",
    val estado: String = "",
    val remitente: String = "",
    val destinatario: String = "",
    val origen: String = "",
    val destino: String = ""
)