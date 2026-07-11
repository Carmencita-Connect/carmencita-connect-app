package com.carmencita.connect.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carmencita.connect.data.NotificacionRepository
import com.carmencita.connect.model.Notificacion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistorialNotificacionesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NotificacionRepository(application)

    private val _notificaciones = MutableLiveData<List<Notificacion>>(emptyList())
    val notificaciones: LiveData<List<Notificacion>> = _notificaciones

    fun cargarHistorial() {
        viewModelScope.launch {
            _notificaciones.value = withContext(Dispatchers.IO) {
                repository.obtenerNotificaciones()
            }
        }
    }

    fun limpiarHistorial() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.limpiar()
            }
            _notificaciones.value = emptyList()
        }
    }
}
