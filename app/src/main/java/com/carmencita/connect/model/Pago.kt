package com.carmencita.connect.model

data class Pago(
    val id: Int = 0,
    val numeroPR: String = "",     // referencia al PreRegistro
    val metodo: String = "",       // "digital", "presencial", "yape"
    val monto: Double = 0.0,
    val estado: String = "",       // "confirmado", "pendiente", "rechazado"
    val fecha: String = ""
)
