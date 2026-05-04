package com.carmencita.connect.model

data class Sede(
    val id: Int = 0,
    val nombre: String = "",
    val direccion: String = "",
    val telefono: String = "",
    val horario: String = "",
    val latitud: Double = 0.0,
    val longitud: Double = 0.0
)