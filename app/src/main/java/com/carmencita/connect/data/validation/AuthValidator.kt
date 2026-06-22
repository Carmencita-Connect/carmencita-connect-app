package com.carmencita.connect.data.validation

object AuthValidator {

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

    fun validarLogin(correo: String, password: String): String {
        if (correo.isBlank() || password.isBlank()) return "Ingresa correo y contraseña"
        if (!esCorreoValido(correo)) return "Ingresa un correo válido"
        return ""
    }

    fun validarRegistro(
        nombre: String,
        dni: String,
        telefono: String,
        correo: String,
        password: String,
        confirmarPassword: String
    ): String {
        if (nombre.isBlank() || dni.isBlank() || telefono.isBlank() ||
            correo.isBlank() || password.isBlank() || confirmarPassword.isBlank()
        ) return "Completa todos los campos"
        if (!nombre.all { it.isLetter() || it.isWhitespace() }) return "El nombre solo debe contener letras"
        if (dni.length != 8 || !dni.all { it.isDigit() }) return "El DNI debe tener 8 dígitos"
        if (telefono.length != 9 || !telefono.all { it.isDigit() }) return "El teléfono debe tener 9 dígitos"
        if (!esCorreoValido(correo)) return "Ingresa un correo válido"
        if (!esPasswordSegura(password)) return "La contraseña debe tener 8 caracteres, letras y números"
        if (password != confirmarPassword) return "Las contraseñas no coinciden"
        return ""
    }

    fun esPasswordSegura(password: String): Boolean {
        return password.length >= 8 &&
            password.any { it.isLetter() } &&
            password.any { it.isDigit() }
    }

    private fun esCorreoValido(correo: String): Boolean {
        return emailRegex.matches(correo.trim())
    }
}
