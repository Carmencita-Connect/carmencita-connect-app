package com.carmencita.connect.data.validation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CodigoConfirmacionTest {

    @Test
    fun generar_siempreRetornaSeisDigitos() {
        repeat(100) {
            val codigo = CodigoConfirmacion.generar()

            assertTrue(codigo.length == 6)
            assertTrue(codigo.all { it.isDigit() })
        }
    }

    @Test
    fun esValido_conSeisDigitos_retornaTrue() {
        assertTrue(CodigoConfirmacion.esValido("012345"))
    }

    @Test
    fun esValido_conLongitudIncorrecta_retornaFalse() {
        assertFalse(CodigoConfirmacion.esValido("12345"))
    }

    @Test
    fun esValido_conLetras_retornaFalse() {
        assertFalse(CodigoConfirmacion.esValido("12A456"))
    }
}
