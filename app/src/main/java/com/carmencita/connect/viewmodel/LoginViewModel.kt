package com.carmencita.connect.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carmencita.connect.data.AuthRepository
import com.carmencita.connect.data.validation.AuthValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository(application)

    private val _loginExitoso = MutableLiveData<Boolean>(false)
    val loginExitoso: LiveData<Boolean> = _loginExitoso

    private val _error = MutableLiveData<String>("")
    val error: LiveData<String> = _error

    private val _cargando = MutableLiveData<Boolean>(false)
    val cargando: LiveData<Boolean> = _cargando

    fun iniciarSesion(correo: String, password: String) {
        val validacion = AuthValidator.validarLogin(correo, password)
        if (validacion.isNotBlank()) {
            _error.value = validacion
            return
        }

        _cargando.value = true
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching {
                    repository.iniciarSesion(correo.trim(), password)
                }
            }
            _cargando.value = false
            result.onSuccess { authResult ->
                if (authResult.exitoso) {
                    _error.value = ""
                    _loginExitoso.value = true
                } else {
                    _error.value = authResult.mensaje
                }
            }.onFailure {
                _error.value = "No se pudo iniciar sesión"
            }
        }
    }

    fun limpiarEstado() {
        _loginExitoso.value = false
        _error.value = ""
    }
}
