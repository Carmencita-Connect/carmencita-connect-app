package com.carmencita.connect.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carmencita.connect.data.AuthRepository
import com.carmencita.connect.data.SesionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SesionViewModel(application: Application) : AndroidViewModel(application) {

    private val sesionRepository = SesionRepository(application)
    private val authRepository = AuthRepository(application)

    private val _sesionActiva = MutableLiveData<Boolean>(false)
    val sesionActiva: LiveData<Boolean> = _sesionActiva

    fun cargarSesion() {
        viewModelScope.launch {
            val activa = withContext(Dispatchers.IO) {
                sesionRepository.haySesionActiva()
            }
            _sesionActiva.value = activa
        }
    }

    fun cerrarSesion() {
        _sesionActiva.value = false
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                authRepository.cerrarSesion()
            }
            _sesionActiva.value = false
        }
    }

    fun entrarComoInvitado() {
        _sesionActiva.value = false
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                authRepository.cerrarSesion()
            }
        }
    }
}
