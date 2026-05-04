package com.carmencita.connect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CotizacionViewModel : ViewModel() {

    private val _costoEstimado = MutableLiveData<Double>()
    val costoEstimado: LiveData<Double> = _costoEstimado

    fun calcularTarifa(alto: Double, largo: Double, ancho: Double, destino: String) {
        // Sprint 1 - S
    }
}