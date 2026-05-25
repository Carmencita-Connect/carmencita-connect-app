package com.carmencita.connect.model

data class PreRegistro(
    val id: Int = 0,
    val remitente: Persona = Persona(),
    val destinatario: Persona = Persona(),
    val descripcionCarga: String = "",
    val encomienda: Encomienda = Encomienda(),
    val estado: String = "pendiente",
    val fechaCreacion: String = ""
)