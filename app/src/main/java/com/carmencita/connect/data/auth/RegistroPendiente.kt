package com.carmencita.connect.data.auth

import com.carmencita.connect.model.Persona

data class RegistroPendiente(
    val persona: Persona,
    val password: String,
    val codigo: String,
    val expiraEn: Long
) {
    fun estaExpirado(ahora: Long = System.currentTimeMillis()): Boolean {
        return ahora > expiraEn
    }
}
