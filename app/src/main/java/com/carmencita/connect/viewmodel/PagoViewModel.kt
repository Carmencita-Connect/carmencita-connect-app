package com.carmencita.connect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PagoViewModel : ViewModel() {

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

    fun seleccionarMetodo(metodo: String) {
        _metodoPago.value = metodo
        _error.value = ""
    }

    fun iniciarPago() {
        val metodo = _metodoPago.value ?: ""
        if (metodo.isEmpty()) {
            _error.value = "Selecciona un método de pago"
            return
        }
        _estado.value = PagoEstado.Validando
    }

    fun onPagoExitoso(token: String) {
        val numero = "PR-2026-%07d".format((1..9999999).random())
        _estado.value = PagoEstado.Confirmado(numero)
    }

    fun onPagoRechazado(mensaje: String) {
        _estado.value = PagoEstado.Rechazado(mensaje)
        _error.value = mensaje
    }

    fun onPagoCancelado() {
        _estado.value = PagoEstado.Cancelado
    }

    fun cancelarPago() {
        _estado.value = PagoEstado.Cancelado
        _metodoPago.value = ""
        _error.value = ""
    }

    fun resetear() {
        _estado.value = PagoEstado.Idle
        _metodoPago.value = ""
        _error.value = ""
    }
}