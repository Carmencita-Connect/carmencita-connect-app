package com.carmencita.connect.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carmencita.connect.data.AuthRepository
import com.carmencita.connect.model.Persona
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistroViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository(application)

    private val _registroExitoso = MutableLiveData<Boolean>(false)
    val registroExitoso: LiveData<Boolean> = _registroExitoso

    private val _error = MutableLiveData<String>("")
    val error: LiveData<String> = _error

    private val _cargando = MutableLiveData<Boolean>(false)
    val cargando: LiveData<Boolean> = _cargando

    fun registrar(
        nombre: String,
        dni: String,
        telefono: String,
        correo: String,
        password: String,
        confirmarPassword: String
    ) {
        val validacion = validarCampos(
            nombre = nombre,
            dni = dni,
            telefono = telefono,
            correo = correo,
            password = password,
            confirmarPassword = confirmarPassword
        )
        if (validacion.isNotEmpty()) {
            _error.value = validacion
            return
        }

        val persona = Persona(
            nombre = nombre.trim(),
            dni = dni.trim(),
            telefono = telefono.trim(),
            direccion = ""
        )

        _cargando.value = true
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching {
                    repository.registrar(persona, correo.trim(), password)
                }
            }
            _cargando.value = false
            result.onSuccess { authResult ->
                if (authResult.exitoso) {
                    _error.value = ""
                    _registroExitoso.value = true
                } else {
                    _error.value = authResult.mensaje
                }
            }.onFailure {
                _error.value = "No se pudo conectar con el servidor"
            }
        }
    }

    fun limpiarEstado() {
        _registroExitoso.value = false
        _error.value = ""
    }

    private fun validarCampos(
        nombre: String,
        dni: String,
        telefono: String,
        correo: String,
        password: String,
        confirmarPassword: String
    ): String {
        if (nombre.isBlank() || dni.isBlank() || telefono.isBlank() ||
            correo.isBlank() || password.isBlank() || confirmarPassword.isBlank()
        ) {
            return "Completa todos los campos"
        }
        if (!nombre.all { it.isLetter() || it.isWhitespace() }) {
            return "El nombre solo debe contener letras"
        }
        if (dni.length != 8 || !dni.all { it.isDigit() }) {
            return "El DNI debe tener 8 dígitos"
        }
        if (telefono.length != 9 || !telefono.all { it.isDigit() }) {
            return "El teléfono debe tener 9 dígitos"
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            return "Ingresa un correo válido"
        }
        if (!esPasswordSegura(password)) {
            return "La contraseña debe tener 8 caracteres, letras y números"
        }
        if (password != confirmarPassword) {
            return "Las contraseñas no coinciden"
        }
        return ""
    }

    private fun esPasswordSegura(password: String): Boolean {
        return password.length >= 8 &&
            password.any { it.isLetter() } &&
            password.any { it.isDigit() }
    }
}
