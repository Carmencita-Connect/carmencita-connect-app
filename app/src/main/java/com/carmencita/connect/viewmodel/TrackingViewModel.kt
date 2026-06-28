package com.carmencita.connect.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carmencita.connect.data.EncomiendaRepository
import com.carmencita.connect.data.HistorialTrackingRepository
import com.carmencita.connect.model.Encomienda
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EncomiendaRepository(application)
    private val historialRepository = HistorialTrackingRepository(application)

    sealed class TrackingEstado {
        object Idle : TrackingEstado()
        object Validando : TrackingEstado()
        object CodigoInvalido : TrackingEstado()
        data class Resultado(val encomienda: Encomienda) : TrackingEstado()
    }

    private val _estado = MutableLiveData<TrackingEstado>(TrackingEstado.Idle)
    val estado: LiveData<TrackingEstado> = _estado

    private val _historial = MutableLiveData(historialRepository.obtenerCodigos())
    val historial: LiveData<List<String>> = _historial

    fun buscarEncomienda(numeroGuia: String) {
        if (numeroGuia.isEmpty()) return

        _estado.value = TrackingEstado.Validando

        viewModelScope.launch {
            delay(2000)
            val encomienda = withContext(Dispatchers.IO) {
                repository.buscarPorGuia(numeroGuia)
            }
            _estado.value = if (encomienda != null) {
                _historial.value = historialRepository.guardarCodigo(encomienda.numeroGuia)
                TrackingEstado.Resultado(encomienda)
            } else {
                TrackingEstado.CodigoInvalido
            }
        }
    }

    fun limpiarHistorial() {
        historialRepository.limpiar()
        _historial.value = emptyList()
    }

    fun resetear() {
        _estado.value = TrackingEstado.Idle
    }
}
