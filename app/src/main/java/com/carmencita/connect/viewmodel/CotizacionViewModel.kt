package com.carmencita.connect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carmencita.connect.data.EncomiendaRepository
import com.carmencita.connect.data.TarifaRepository
import com.carmencita.connect.model.Encomienda
import com.carmencita.connect.model.Tarifa

class CotizacionViewModel : ViewModel() {

    private val tarifaRepository = TarifaRepository()
    private val encomiendaRepository = EncomiendaRepository()

    private val _costoEstimado = MutableLiveData<Double>()
    val costoEstimado: LiveData<Double> = _costoEstimado

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Datos expuestos para PreRegistroFragment
    private val _destinoSeleccionado = MutableLiveData<String>()
    val destinoSeleccionado: LiveData<String> = _destinoSeleccionado

    private val _largo = MutableLiveData<Double>()
    val largo: LiveData<Double> = _largo

    private val _ancho = MutableLiveData<Double>()
    val ancho: LiveData<Double> = _ancho

    private val _alto = MutableLiveData<Double>()
    val alto: LiveData<Double> = _alto

    private val _peso = MutableLiveData<Double>()
    val peso: LiveData<Double> = _peso

    private val _tarifa = MutableLiveData<Tarifa>()
    val tarifa: LiveData<Tarifa> = _tarifa

    private val _encomiendaCotizada = MutableLiveData<Encomienda?>()
    val encomiendaCotizada: LiveData<Encomienda?> = _encomiendaCotizada

    fun calcularTarifa(
        largo: String,
        ancho: String,
        alto: String,
        peso: String,
        origen: String,
        destino: String
    ) {
        if (largo.isEmpty() || ancho.isEmpty() ||
            alto.isEmpty() || peso.isEmpty()) {
            _error.value = "Completa todas las medidas"
            return
        }

        val largoD = largo.toDoubleOrNull()
        val anchoD = ancho.toDoubleOrNull()
        val altoD  = alto.toDoubleOrNull()
        val pesoD  = peso.toDoubleOrNull()

        if (largoD == null || largoD <= 0 ||
            anchoD == null || anchoD <= 0 ||
            altoD  == null || altoD  <= 0) {
            _error.value = "Las medidas deben ser números positivos"
            return
        }

        if (pesoD == null || pesoD <= 0) {
            _error.value = "El peso debe ser un número positivo"
            return
        }

        if (origen == destino) {
            _error.value = "El origen y destino no pueden ser iguales"
            return
        }

        _largo.value = largoD
        _ancho.value = anchoD
        _alto.value  = altoD
        _peso.value  = pesoD
        _destinoSeleccionado.value = destino

        val tarifa = tarifaRepository.calcularTarifa(largoD, anchoD, altoD, pesoD, destino)
        val encomienda = encomiendaRepository.crearCotizada(
            largo = largoD,
            ancho = anchoD,
            alto = altoD,
            peso = pesoD,
            origen = origen,
            destino = destino,
            tarifa = tarifa
        )
        _error.value = ""
        _tarifa.value = tarifa
        _costoEstimado.value = tarifa.costo
        _encomiendaCotizada.value = encomienda
    }

    fun resetear() {
        _costoEstimado.value = 0.0
        _error.value = ""
        _destinoSeleccionado.value = ""
        _largo.value = 0.0
        _ancho.value = 0.0
        _alto.value = 0.0
        _peso.value = 0.0
        _tarifa.value = Tarifa()
        _encomiendaCotizada.value = null
    }
}
