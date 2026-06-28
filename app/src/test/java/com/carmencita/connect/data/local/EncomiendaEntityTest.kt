package com.carmencita.connect.data.local

import org.junit.Assert.assertEquals
import org.junit.Test

class EncomiendaEntityTest {

    @Test
    fun `convierte la fila de room al modelo de dominio`() {
        val entity = EncomiendaEntity(
            id = 2,
            numeroGuia = "C000000345",
            largo = 35.0,
            ancho = 25.0,
            alto = 18.0,
            peso = 7.5,
            origen = "Lima",
            destino = "Trujillo",
            tarifaDestino = "Trujillo",
            tarifaCosto = 38.0,
            estado = "EN TRÁNSITO",
            fechaRegistro = "18/06/26",
            fechaTransito = "19/06/26",
            fechaAgencia = null,
            fechaEntrega = null
        )

        val encomienda = entity.toModel()

        assertEquals(2, encomienda.id)
        assertEquals("C000000345", encomienda.numeroGuia)
        assertEquals("Trujillo", encomienda.tarifa.destino)
        assertEquals(38.0, encomienda.tarifa.costo, 0.0)
    }
}
