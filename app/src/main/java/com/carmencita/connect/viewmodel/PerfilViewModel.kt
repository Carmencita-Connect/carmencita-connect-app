package com.carmencita.connect.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carmencita.connect.data.PersonaRepository
import com.carmencita.connect.model.Persona
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PersonaRepository(application)

    private val _persona = MutableLiveData<Persona?>()
    val persona: LiveData<Persona?> = _persona

    private val _error = MutableLiveData<String>("")
    val error: LiveData<String> = _error

    private val _mensaje = MutableLiveData<String>("")
    val mensaje: LiveData<String> = _mensaje

    private val _cargando = MutableLiveData<Boolean>(false)
    val cargando: LiveData<Boolean> = _cargando

    fun cargarPerfil() {
        _cargando.value = true
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching { repository.obtenerPersonaActual() }
            }
            _cargando.value = false
            result.onSuccess { personaResult ->
                if (personaResult.exitoso) {
                    _error.value = ""
                    _persona.value = personaResult.persona
                } else {
                    _error.value = personaResult.mensaje
                }
            }.onFailure {
                _error.value = "No se pudo cargar el perfil"
            }
        }
    }

    fun actualizarTelefono(telefono: String) {
        val nombreActual = _persona.value?.nombre.orEmpty()
        actualizarPerfil(nombreActual, telefono)
    }

    fun actualizarPerfil(nombre: String, telefono: String) {
        if (nombre.isBlank()) {
            _error.value = "El nombre no puede estar vacio"
            return
        }
        if (!nombre.trim().all { it.isLetter() || it.isWhitespace() }) {
            _error.value = "El nombre solo debe contener letras"
            return
        }
        if (telefono.length != 9 || !telefono.all { it.isDigit() }) {
            _error.value = "El telefono debe tener 9 digitos"
            return
        }

        _cargando.value = true
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching { repository.actualizarPerfil(nombre.trim(), telefono.trim()) }
            }
            _cargando.value = false
            result.onSuccess { personaResult ->
                if (personaResult.exitoso) {
                    _error.value = ""
                    _mensaje.value = personaResult.mensaje
                    _persona.value = personaResult.persona
                } else {
                    _error.value = personaResult.mensaje
                }
            }.onFailure {
                _error.value = "No se pudo actualizar el perfil"
            }
        }
    }

    fun limpiarPerfil() {
        _persona.value = null
        limpiarMensajes()
    }

    fun limpiarMensajes() {
        _error.value = ""
        _mensaje.value = ""
    }
}
