package com.carmencita.connect.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carmencita.connect.data.AuthRepository
import com.carmencita.connect.data.validation.AuthValidator
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
        val validacion = AuthValidator.validarRegistro(
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
                _error.value = "No se pudo registrar el usuario"
            }
        }
    }

    fun limpiarEstado() {
        _registroExitoso.value = false
        _error.value = ""
    }
}
