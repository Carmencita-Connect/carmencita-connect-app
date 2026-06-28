package com.carmencita.connect.data.auth

import com.carmencita.connect.model.Persona
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RegistroPendienteTest {

    @Test
    fun estaExpirado_cuandoSuperaFechaLimite_retornaTrue() {
        val pendiente = crearPendiente(expiraEn = 1_000)

        assertTrue(pendiente.estaExpirado(ahora = 1_001))
    }

    @Test
    fun estaExpirado_antesDeFechaLimite_retornaFalse() {
        val pendiente = crearPendiente(expiraEn = 1_000)

        assertFalse(pendiente.estaExpirado(ahora = 999))
    }

    private fun crearPendiente(expiraEn: Long): RegistroPendiente {
        return RegistroPendiente(
            persona = Persona(
                nombre = "Angel Yepez",
                dni = "12345678",
                telefono = "954684440",
                correo = "angel@example.com"
            ),
            password = "Clave123",
            codigo = "123456",
            expiraEn = expiraEn
        )
    }
}
