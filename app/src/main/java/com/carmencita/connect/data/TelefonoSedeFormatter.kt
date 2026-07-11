package com.carmencita.connect.data

object TelefonoSedeFormatter {

    fun normalizarParaMarcador(telefono: String): String? {
        val telefonoLimpio = telefono.trim()
        val digitos = telefonoLimpio.filter(Char::isDigit)

        if (digitos.length < MINIMO_DIGITOS_TELEFONO) return null

        return if (telefonoLimpio.startsWith("+")) {
            "+$digitos"
        } else {
            digitos
        }
    }

    private const val MINIMO_DIGITOS_TELEFONO = 7
}
