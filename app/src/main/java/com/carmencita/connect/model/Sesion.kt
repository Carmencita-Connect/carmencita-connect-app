package com.carmencita.connect.model

data class Sesion(
    val token: String = "",
    val correo: String = "",
    val activa: Boolean = false
)
