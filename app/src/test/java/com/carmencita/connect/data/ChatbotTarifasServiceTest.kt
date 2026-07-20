package com.carmencita.connect.data

import org.junit.Assert.assertTrue
import org.junit.Test

class ChatbotTarifasServiceTest {

    private val chatbotService = ChatbotTarifasService()

    @Test
    fun responder_calculaTarifaCuandoRecibeDatosCompletos() {
        val respuesta = chatbotService.responder(
            "cotizar largo 30 ancho 20 alto 15 peso 5 origen Trujillo destino Angasmarca"
        )

        assertTrue(respuesta.contains("S/ 41.50"))
    }

    @Test
    fun responder_solicitaFormatoCuandoFaltanDatosDeCotizacion() {
        val respuesta = chatbotService.responder("Cuanto cuesta mi envio?")

        assertTrue(respuesta.contains("Para calcular una tarifa"))
    }

    @Test
    fun responder_contestaPreguntaFrecuenteDeHorarios() {
        val respuesta = chatbotService.responder("Cual es el horario de atencion?")

        assertTrue(respuesta.contains("8:00 am"))
    }

    @Test
    fun responder_priorizaTrackingSobreConsultaGeneralDeEncomienda() {
        val respuesta = chatbotService.responder("Como rastreo mi encomienda?")

        assertTrue(respuesta.contains("Tracking"))
    }

    @Test
    fun responder_priorizaLlamadaSobreConsultaGeneralDeSedes() {
        val respuesta = chatbotService.responder("Como llamo a una sede?")

        assertTrue(respuesta.contains("Llamar sede"))
    }

    @Test
    fun responder_priorizaArticulosRestringidosSobreConsultaGeneralDeEnvio() {
        val respuesta = chatbotService.responder("Que articulos no puedo enviar?")

        assertTrue(respuesta.contains("No se deben enviar"))
    }

    @Test
    fun responder_contestaPreguntaFrecuenteDePago() {
        val respuesta = chatbotService.responder("Como puedo pagar?")

        assertTrue(respuesta.contains("pago digital"))
    }

    @Test
    fun responder_validaOrigenYDestinoDiferentes() {
        val respuesta = chatbotService.responder(
            "cotizar largo 30 ancho 20 alto 15 peso 5 origen Trujillo destino Trujillo"
        )

        assertTrue(respuesta.contains("origen y destino"))
    }
}
