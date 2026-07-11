package com.carmencita.connect.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AlertaEstadoPolicyTest {

    @Test
    fun siguienteEstado_avanzaSegunElFlujoDeEnvio() {
        assertEquals(
            AlertaEstadoPolicy.ESTADO_TRANSITO,
            AlertaEstadoPolicy.siguienteEstado(AlertaEstadoPolicy.ESTADO_REGISTRADO)
        )
        assertEquals(
            AlertaEstadoPolicy.ESTADO_AGENCIA,
            AlertaEstadoPolicy.siguienteEstado(AlertaEstadoPolicy.ESTADO_TRANSITO)
        )
        assertEquals(
            AlertaEstadoPolicy.ESTADO_ENTREGADO,
            AlertaEstadoPolicy.siguienteEstado(AlertaEstadoPolicy.ESTADO_AGENCIA)
        )
    }

    @Test
    fun siguienteEstado_reiniciaCuandoYaEstaEntregado() {
        assertEquals(
            AlertaEstadoPolicy.ESTADO_REGISTRADO,
            AlertaEstadoPolicy.siguienteEstado(AlertaEstadoPolicy.ESTADO_ENTREGADO)
        )
    }

    @Test
    fun mensajeParaEstado_indicaRecojoCuandoEstaEnAgencia() {
        val mensaje = AlertaEstadoPolicy.mensajeParaEstado(
            "C000000001",
            AlertaEstadoPolicy.ESTADO_AGENCIA
        )

        assertTrue(mensaje.contains("recogerla"))
    }
}
