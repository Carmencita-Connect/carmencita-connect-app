package com.carmencita.connect.data

object ReglasEstadoEnvio {

    const val ESTADO_REGISTRADO = "REGISTRADO"
    const val ESTADO_TRANSITO = "EN TRANSITO"
    const val ESTADO_AGENCIA = "EN AGENCIA"
    const val ESTADO_ENTREGADO = "ENTREGADO"

    private val FLUJO_ESTADOS = listOf(
        ESTADO_REGISTRADO,
        ESTADO_TRANSITO,
        ESTADO_AGENCIA,
        ESTADO_ENTREGADO
    )

    fun siguienteEstado(estadoActual: String): String {
        val indiceActual = FLUJO_ESTADOS.indexOf(estadoActual.uppercase())
        if (indiceActual == -1) return ESTADO_REGISTRADO

        val siguienteIndice = (indiceActual + 1) % FLUJO_ESTADOS.size
        return FLUJO_ESTADOS[siguienteIndice]
    }

    fun mensajeParaEstado(numeroGuia: String, estado: String): String {
        return if (estado == ESTADO_AGENCIA) {
            "Tu encomienda $numeroGuia ya esta en agencia. Puedes acercarte a recogerla."
        } else {
            "Tu encomienda $numeroGuia cambio a estado $estado."
        }
    }
}
