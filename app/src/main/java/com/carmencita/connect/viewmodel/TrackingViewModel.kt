package com.carmencita.connect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carmencita.connect.data.EncomiendaRepository
import com.carmencita.connect.model.Encomienda

class TrackingViewModel : ViewModel() {

    private val repository = EncomiendaRepository()

    sealed class TrackingEstado {
        object Idle : TrackingEstado()
        object Validando : TrackingEstado()
        object CodigoInvalido : TrackingEstado()
        data class Resultado(val encomienda: Encomienda) : TrackingEstado()
    }

    private val _estado = MutableLiveData<TrackingEstado>(TrackingEstado.Idle)
    val estado: LiveData<TrackingEstado> = _estado

    fun buscarEncomienda(numeroGuia: String) {
        if (numeroGuia.isEmpty()) return

        _estado.value = TrackingEstado.Validando

        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            val encomienda = repository.buscarPorGuia(numeroGuia)
            _estado.value = if (encomienda != null)
                TrackingEstado.Resultado(encomienda)
            else
                TrackingEstado.CodigoInvalido
        }, 2000)
    }

    fun resetear() {
        _estado.value = TrackingEstado.Idle
    }
}