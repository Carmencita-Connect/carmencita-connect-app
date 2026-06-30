package com.carmencita.connect.model

data class Pago(
    val id: Int = 0,
    val numeroPR: String = "",
    val metodo: String = "",
    val monto: Double = 0.0,
    val estado: String = "",
    val fecha: String = ""
)
