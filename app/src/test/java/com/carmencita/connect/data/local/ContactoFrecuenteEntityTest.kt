package com.carmencita.connect.data.local

import org.junit.Assert.assertEquals
import org.junit.Test

class ContactoFrecuenteEntityTest {

    @Test
    fun `convierte entidad de room a persona`() {
        val entity = ContactoFrecuenteEntity(
            id = 10,
            personaId = 2,
            nombre = "Maria Lopez",
            dni = "12345678",
            telefono = "999999999",
            direccion = "Jr. Pizarro 123"
        )

        val contacto = entity.toPersona()

        assertEquals("Maria Lopez", contacto.nombre)
        assertEquals("12345678", contacto.dni)
        assertEquals("999999999", contacto.telefono)
        assertEquals("Jr. Pizarro 123", contacto.direccion)
    }
}
