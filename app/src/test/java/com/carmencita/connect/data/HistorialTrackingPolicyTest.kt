package com.carmencita.connect.data

import org.junit.Assert.assertEquals
import org.junit.Test

class HistorialTrackingPolicyTest {

    @Test
    fun `agrega el codigo normalizado al inicio`() {
        val resultado = HistorialTrackingPolicy.agregar(
            listOf("C000000001"),
            " c000000345 "
        )

        assertEquals(listOf("C000000345", "C000000001"), resultado)
    }

    @Test
    fun `mueve un codigo repetido al inicio sin duplicarlo`() {
        val resultado = HistorialTrackingPolicy.agregar(
            listOf("C000000001", "C000000345", "C000000782"),
            "c000000345"
        )

        assertEquals(
            listOf("C000000345", "C000000001", "C000000782"),
            resultado
        )
    }

    @Test
    fun `conserva como maximo cinco codigos`() {
        val resultado = HistorialTrackingPolicy.agregar(
            listOf(
                "C000000001",
                "C000000002",
                "C000000003",
                "C000000004",
                "C000000005"
            ),
            "C000000006"
        )

        assertEquals(
            listOf(
                "C000000006",
                "C000000001",
                "C000000002",
                "C000000003",
                "C000000004"
            ),
            resultado
        )
    }

    @Test
    fun `ignora codigos vacios`() {
        val codigos = listOf("C000000001")

        assertEquals(codigos, HistorialTrackingPolicy.agregar(codigos, "  "))
    }
}
