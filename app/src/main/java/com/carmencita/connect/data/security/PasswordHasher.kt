package com.carmencita.connect.data.security

import java.security.MessageDigest
import java.security.SecureRandom

object PasswordHasher {

    fun generarSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return bytes.joinToString("") { "%02x".format(it.toInt() and 0xff) }
    }

    fun hash(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest("$salt:$password".toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it.toInt() and 0xff) }
    }

    fun verificar(password: String, salt: String, esperado: String): Boolean {
        return hash(password, salt) == esperado
    }
}
