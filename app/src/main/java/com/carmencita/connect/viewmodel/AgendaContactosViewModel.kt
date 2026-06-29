package com.carmencita.connect.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carmencita.connect.data.ContactoRepository
import com.carmencita.connect.model.Persona
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AgendaContactosViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ContactoRepository(application)

    private val _contactos = MutableLiveData<List<Persona>>(emptyList())
    val contactos: LiveData<List<Persona>> = _contactos

    private val _contactoSeleccionado = MutableLiveData<Persona?>()
    val contactoSeleccionado: LiveData<Persona?> = _contactoSeleccionado

    private val _contactoEnEdicion = MutableLiveData<Persona?>()
    val contactoEnEdicion: LiveData<Persona?> = _contactoEnEdicion

    private val _error = MutableLiveData("")
    val error: LiveData<String> = _error

    private val _mensaje = MutableLiveData("")
    val mensaje: LiveData<String> = _mensaje

    private val _cargando = MutableLiveData(false)
    val cargando: LiveData<Boolean> = _cargando

    fun cargarContactos() {
        ejecutarLista { repository.listar() }
    }

    fun buscar(texto: String) {
        ejecutarLista { repository.buscar(texto) }
    }

    fun guardar(nombre: String, dni: String, telefono: String, direccion: String) {
        _cargando.value = true
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.guardar(nombre, dni, telefono, direccion)
            }
            _cargando.value = false
            if (result.exitoso) {
                _error.value = ""
                _mensaje.value = result.mensaje
                cargarContactos()
            } else {
                _mensaje.value = ""
                _error.value = result.mensaje
            }
        }
    }

    fun prepararEdicion(contacto: Persona) {
        _contactoEnEdicion.value = contacto
        limpiarMensajes()
    }

    fun actualizarTelefonoContacto(telefono: String) {
        val contacto = _contactoEnEdicion.value ?: run {
            _error.value = "No se encontro el contacto"
            return
        }

        _cargando.value = true
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.actualizarTelefono(contacto.dni, telefono)
            }
            _cargando.value = false
            if (result.exitoso) {
                _error.value = ""
                _mensaje.value = result.mensaje
                _contactoEnEdicion.value = result.contacto
                cargarContactos()
            } else {
                _mensaje.value = ""
                _error.value = result.mensaje
            }
        }
    }

    fun eliminar(contacto: Persona) {
        _cargando.value = true
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.eliminar(contacto.dni)
            }
            _cargando.value = false
            if (result.exitoso) {
                _error.value = ""
                _mensaje.value = result.mensaje
                cargarContactos()
            } else {
                _mensaje.value = ""
                _error.value = result.mensaje
            }
        }
    }

    fun seleccionar(contacto: Persona) {
        _contactoSeleccionado.value = contacto
    }

    fun limpiarSeleccion() {
        _contactoSeleccionado.value = null
    }

    fun limpiarMensajes() {
        _error.value = ""
        _mensaje.value = ""
    }

    private fun ejecutarLista(accion: () -> ContactoRepository.ContactoResult) {
        _cargando.value = true
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { accion() }
            _cargando.value = false
            if (result.exitoso) {
                _error.value = ""
                _contactos.value = result.contactos
            } else {
                _contactos.value = emptyList()
                _error.value = result.mensaje
            }
        }
    }
}
