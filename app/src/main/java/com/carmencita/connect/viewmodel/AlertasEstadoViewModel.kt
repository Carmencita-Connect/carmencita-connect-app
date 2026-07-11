package com.carmencita.connect.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carmencita.connect.data.AlertaEstadoRepository
import com.carmencita.connect.data.NotificacionRepository
import com.carmencita.connect.model.AlertaCambioEstado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertasEstadoViewModel(application: Application) : AndroidViewModel(application) {

    private val alertaRepository = AlertaEstadoRepository(application)
    private val notificacionRepository = NotificacionRepository(application)

    private val _alertasActivas = MutableLiveData(false)
    val alertasActivas: LiveData<Boolean> = _alertasActivas

    private val _estadoActual = MutableLiveData("")
    val estadoActual: LiveData<String> = _estadoActual

    private val _ultimoCambio = MutableLiveData<AlertaCambioEstado?>()
    val ultimoCambio: LiveData<AlertaCambioEstado?> = _ultimoCambio

    private val _mensaje = MutableLiveData("")
    val mensaje: LiveData<String> = _mensaje

    fun cargarConfiguracion() {
        viewModelScope.launch {
            val configuracion = withContext(Dispatchers.IO) {
                alertaRepository.alertasActivas() to alertaRepository.estadoActual()
            }
            _alertasActivas.value = configuracion.first
            _estadoActual.value = configuracion.second
        }
    }

    fun configurarAlertas(activo: Boolean) {
        _alertasActivas.value = activo
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                alertaRepository.configurarAlertas(activo)
            }
            _mensaje.value = if (activo) {
                "Alertas activadas"
            } else {
                "Alertas desactivadas"
            }
        }
    }

    fun simularCambioEstado() {
        viewModelScope.launch {
            val resultado = withContext(Dispatchers.IO) {
                val cambio = alertaRepository.simularCambioEstado()
                if (cambio != null && alertaRepository.alertasActivas()) {
                    notificacionRepository.registrar(
                        titulo = "Cambio de estado",
                        mensaje = cambio.mensaje,
                        estado = cambio.estadoNuevo
                    )
                }
                cambio
            }

            resultado ?: return@launch
            _estadoActual.value = resultado.estadoNuevo
            _ultimoCambio.value = resultado
            _mensaje.value = if (_alertasActivas.value == true) {
                "Se genero una alerta y se guardo en el historial"
            } else {
                "El estado cambio, pero las alertas estan desactivadas"
            }
        }
    }
}
