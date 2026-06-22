package com.carmencita.connect.data.validation

import java.security.SecureRandom

object CodigoConfirmacion {

    private val secureRandom = SecureRandom()

    fun generar(): String {
        return secureRandom.nextInt(1_000_000).toString().padStart(6, '0')
    }

    fun esValido(codigo: String): Boolean {
        return codigo.length == 6 && codigo.all { it.isDigit() }
    }
}
