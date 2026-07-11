package com.carmencita.connect.model

data class Alerta(
    val numeroGuia: String,
    val estadoAnterior: String,
    val estadoNuevo: String,
    val mensaje: String,
    val requiereRecojo: Boolean
)
