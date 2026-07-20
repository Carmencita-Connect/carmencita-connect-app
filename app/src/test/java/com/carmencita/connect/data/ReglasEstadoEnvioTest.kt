package com.carmencita.connect.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReglasEstadoEnvioTest {

    @Test
    fun siguienteEstado_avanzaSegunElFlujoDeEnvio() {
        assertEquals(
            ReglasEstadoEnvio.ESTADO_TRANSITO,
            ReglasEstadoEnvio.siguienteEstado(ReglasEstadoEnvio.ESTADO_REGISTRADO)
        )
        assertEquals(
            ReglasEstadoEnvio.ESTADO_AGENCIA,
            ReglasEstadoEnvio.siguienteEstado(ReglasEstadoEnvio.ESTADO_TRANSITO)
        )
        assertEquals(
            ReglasEstadoEnvio.ESTADO_ENTREGADO,
            ReglasEstadoEnvio.siguienteEstado(ReglasEstadoEnvio.ESTADO_AGENCIA)
        )
    }

    @Test
    fun siguienteEstado_reiniciaCuandoYaEstaEntregado() {
        assertEquals(
            ReglasEstadoEnvio.ESTADO_REGISTRADO,
            ReglasEstadoEnvio.siguienteEstado(ReglasEstadoEnvio.ESTADO_ENTREGADO)
        )
    }

    @Test
    fun mensajeParaEstado_indicaRecojoCuandoEstaEnAgencia() {
        val mensaje = ReglasEstadoEnvio.mensajeParaEstado(
            "C000000001",
            ReglasEstadoEnvio.ESTADO_AGENCIA
        )

        assertTrue(mensaje.contains("recogerla"))
    }
}
