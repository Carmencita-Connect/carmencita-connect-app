package com.carmencita.connect.data

import com.carmencita.connect.model.Notificacion
import org.junit.Assert.assertEquals
import org.junit.Test

class HistorialNotificacionPolicyTest {

    @Test
    fun agregar_colocaLaNuevaNotificacionAlInicio() {
        val actual = notificacion("1")
        val nueva = notificacion("2")

        val resultado = HistorialNotificacionPolicy.agregar(listOf(actual), nueva)

        assertEquals(listOf(nueva, actual), resultado)
    }

    @Test
    fun agregar_reemplazaNotificacionConElMismoId() {
        val anterior = notificacion("1", titulo = "Anterior")
        val actualizada = notificacion("1", titulo = "Actualizada")

        val resultado = HistorialNotificacionPolicy.agregar(listOf(anterior), actualizada)

        assertEquals(listOf(actualizada), resultado)
    }

    @Test
    fun agregar_respetaElLimiteDelHistorial() {
        val historial = (1..20).map { notificacion("$it") }
        val nueva = notificacion("21")

        val resultado = HistorialNotificacionPolicy.agregar(historial, nueva)

        assertEquals(20, resultado.size)
        assertEquals("21", resultado.first().id)
    }

    @Test
    fun agregar_ignoraNotificacionSinTitulo() {
        val historial = listOf(notificacion("1"))
        val invalida = notificacion("2", titulo = "")

        val resultado = HistorialNotificacionPolicy.agregar(historial, invalida)

        assertEquals(historial, resultado)
    }

    private fun notificacion(
        id: String,
        titulo: String = "Cambio de estado"
    ) = Notificacion(
        id = id,
        titulo = titulo,
        mensaje = "Mensaje de prueba",
        fecha = "21/06/2026 09:30",
        estado = "EN AGENCIA"
    )
}
