package com.carmencita.connect.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SedeRepositoryTest {

    private val repository = SedeRepository()

    @Test
    fun obtenerSedes_devuelveLasTresAgenciasDeLaHu13() {
        val sedes = repository.obtenerSedes()

        assertEquals(3, sedes.size)
        assertEquals(
            listOf(
                "Agencia Trujillo",
                "Agencia Angasmarca",
                "Agencia Santiago de Surco"
            ),
            sedes.map { it.nombre }
        )
        assertTrue(sedes.all { it.direccion.isNotBlank() })
        assertTrue(sedes.all { it.telefono.isNotBlank() })
        assertTrue(sedes.all { it.horario.isNotBlank() })
    }
}
