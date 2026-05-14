package com.carmencita.connect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carmencita.connect.data.TarifaRepository

class CotizacionViewModel : ViewModel() {

    private val repository = TarifaRepository()

    private val _costoEstimado = MutableLiveData<Double>()
    val costoEstimado: LiveData<Double> = _costoEstimado

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun calcularTarifa(
        largo: String,
        ancho: String,
        alto: String,
        peso: String,
        origen: String,
        destino: String
    ) {

        if (largo.isEmpty() || ancho.isEmpty() ||
            alto.isEmpty()  || peso.isEmpty()) {
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

        val total = repository.calcularTarifa(
            largoD, anchoD, altoD, pesoD, destino
        )

        _error.value = ""
        _costoEstimado.value = total
    }
}