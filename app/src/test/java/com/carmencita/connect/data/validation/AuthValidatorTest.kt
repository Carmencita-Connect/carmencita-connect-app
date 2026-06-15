package com.carmencita.connect.data.validation

import org.junit.Assert.assertEquals
import org.junit.Test

class AuthValidatorTest {

    @Test
    fun validarLogin_conCamposVacios_retornaError() {
        val error = AuthValidator.validarLogin("", "")

        assertEquals("Ingresa correo y contraseña", error)
    }

    @Test
    fun validarLogin_conCorreoInvalido_retornaError() {
        val error = AuthValidator.validarLogin("correo-invalido", "Clave123")

        assertEquals("Ingresa un correo válido", error)
    }

    @Test
    fun validarLogin_conDatosValidos_noRetornaError() {
        val error = AuthValidator.validarLogin("angel@example.com", "Clave123")

        assertEquals("", error)
    }

    @Test
    fun validarRegistro_conCamposVacios_retornaError() {
        val error = AuthValidator.validarRegistro("", "", "", "", "", "")

        assertEquals("Completa todos los campos", error)
    }

    @Test
    fun validarRegistro_conDniInvalido_retornaError() {
        val error = AuthValidator.validarRegistro(
            nombre = "Angel Yepez",
            dni = "123",
            telefono = "954684440",
            correo = "angel@example.com",
            password = "Clave123",
            confirmarPassword = "Clave123"
        )

        assertEquals("El DNI debe tener 8 dígitos", error)
    }

    @Test
    fun validarRegistro_conTelefonoInvalido_retornaError() {
        val error = AuthValidator.validarRegistro(
            nombre = "Angel Yepez",
            dni = "12345678",
            telefono = "954",
            correo = "angel@example.com",
            password = "Clave123",
            confirmarPassword = "Clave123"
        )

        assertEquals("El teléfono debe tener 9 dígitos", error)
    }

    @Test
    fun validarRegistro_conPasswordDebil_retornaError() {
        val error = AuthValidator.validarRegistro(
            nombre = "Angel Yepez",
            dni = "12345678",
            telefono = "954684440",
            correo = "angel@example.com",
            password = "password",
            confirmarPassword = "password"
        )

        assertEquals("La contraseña debe tener 8 caracteres, letras y números", error)
    }

    @Test
    fun validarRegistro_conPasswordsDiferentes_retornaError() {
        val error = AuthValidator.validarRegistro(
            nombre = "Angel Yepez",
            dni = "12345678",
            telefono = "954684440",
            correo = "angel@example.com",
            password = "Clave123",
            confirmarPassword = "Clave124"
        )

        assertEquals("Las contraseñas no coinciden", error)
    }

    @Test
    fun validarRegistro_conDatosValidos_noRetornaError() {
        val error = AuthValidator.validarRegistro(
            nombre = "Angel Yepez",
            dni = "12345678",
            telefono = "954684440",
            correo = "angel@example.com",
            password = "Clave123",
            confirmarPassword = "Clave123"
        )

        assertEquals("", error)
    }
}
