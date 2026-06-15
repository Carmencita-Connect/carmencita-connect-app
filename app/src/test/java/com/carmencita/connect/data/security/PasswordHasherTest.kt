package com.carmencita.connect.data.security

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordHasherTest {

    @Test
    fun verificar_conPasswordCorrecta_retornaTrue() {
        val salt = PasswordHasher.generarSalt()
        val hash = PasswordHasher.hash("Clave123", salt)

        assertTrue(PasswordHasher.verificar("Clave123", salt, hash))
    }

    @Test
    fun verificar_conPasswordIncorrecta_retornaFalse() {
        val salt = PasswordHasher.generarSalt()
        val hash = PasswordHasher.hash("Clave123", salt)

        assertFalse(PasswordHasher.verificar("Otra123", salt, hash))
    }

    @Test
    fun hash_conSaltDistinto_generaHashDistinto() {
        val hashUno = PasswordHasher.hash("Clave123", PasswordHasher.generarSalt())
        val hashDos = PasswordHasher.hash("Clave123", PasswordHasher.generarSalt())

        assertNotEquals(hashUno, hashDos)
    }
}
