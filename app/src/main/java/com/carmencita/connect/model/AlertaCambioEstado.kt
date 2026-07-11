package com.carmencita.connect.model

data class AlertaCambioEstado(
    val numeroGuia: String,
    val estadoAnterior: String,
    val estadoNuevo: String,
    val mensaje: String,
    val requiereRecojo: Boolean
)
