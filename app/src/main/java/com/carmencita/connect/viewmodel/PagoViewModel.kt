package com.carmencita.connect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carmencita.connect.data.PagoRepository
import com.carmencita.connect.model.Pago

class PagoViewModel : ViewModel() {

    private val repository = PagoRepository()

    sealed class PagoEstado {
        object Idle : PagoEstado()
        object Validando : PagoEstado()
        data class Confirmado(val numeroPR: String) : PagoEstado()
        data class Rechazado(val mensaje: String) : PagoEstado()
        object Cancelado : PagoEstado()
    }

    private val _estado = MutableLiveData<PagoEstado>(PagoEstado.Idle)
    val estado: LiveData<PagoEstado> = _estado

    private val _error = MutableLiveData<String>("")
    val error: LiveData<String> = _error

    private val _metodoPago = MutableLiveData<String>("")
    val metodoPago: LiveData<String> = _metodoPago

    private val _mostrarTarjeta = MutableLiveData<Boolean>(false)
    val mostrarTarjeta: LiveData<Boolean> = _mostrarTarjeta

    // Pago generado
    private val _pagoGenerado = MutableLiveData<Pago?>()
    val pagoGenerado: LiveData<Pago?> = _pagoGenerado

    fun seleccionarMetodo(metodo: String) {
        _metodoPago.value = metodo
        _error.value = ""
        _mostrarTarjeta.value = metodo == "Tarjeta de crédito / débito"
    }

    fun onPagoExitoso(token: String, monto: Double) {
        val pago = repository.procesarPago(
            metodo = _metodoPago.value ?: "",
            monto  = monto
        )
        _pagoGenerado.value = pago
        _estado.value = PagoEstado.Confirmado(pago.numeroPR)
    }

    fun onPagoRechazado(mensaje: String) {
        _estado.value = PagoEstado.Rechazado(mensaje)
        _error.value = mensaje
    }

    fun cancelarPago() {
        _estado.value = PagoEstado.Cancelado
        _metodoPago.value = ""
        _error.value = ""
        _mostrarTarjeta.value = false
        _pagoGenerado.value = null
    }

    fun resetear() {
        _estado.value = PagoEstado.Idle
        _metodoPago.value = ""
        _error.value = ""
        _mostrarTarjeta.value = false
        _pagoGenerado.value = null
    }

}