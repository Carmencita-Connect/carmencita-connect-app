package com.carmencita.connect.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TelefonoSedeFormatterTest {

    @Test
    fun normalizarParaMarcador_eliminaEspaciosDelTelefono() {
        val numero = TelefonoSedeFormatter.normalizarParaMarcador("999 999 999")

        assertEquals("999999999", numero)
    }

    @Test
    fun normalizarParaMarcador_aceptaTelefonoFijoConCodigoDeCiudad() {
        val numero = TelefonoSedeFormatter.normalizarParaMarcador("(044) 123456")

        assertEquals("044123456", numero)
    }

    @Test
    fun normalizarParaMarcador_conservaPrefijoInternacional() {
        val numero = TelefonoSedeFormatter.normalizarParaMarcador("+51 999 999 999")

        assertEquals("+51999999999", numero)
    }

    @Test
    fun normalizarParaMarcador_devuelveNullSiNoHayNumeroValido() {
        val numero = TelefonoSedeFormatter.normalizarParaMarcador("S/N")

        assertNull(numero)
    }
}
