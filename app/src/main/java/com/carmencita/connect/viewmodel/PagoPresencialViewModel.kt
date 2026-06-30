package com.carmencita.connect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carmencita.connect.data.PagoRepository
import com.carmencita.connect.model.Pago

class PagoPresencialViewModel : ViewModel() {

    private val repository = PagoRepository()

    private val _pagoConfirmado = MutableLiveData<Pago?>()
    val pagoConfirmado: LiveData<Pago?> = _pagoConfirmado

    fun confirmarPago(monto: Double) {
        val pago = repository.procesarPago(
            metodo = "agencia",
            monto  = monto
        )
        _pagoConfirmado.value = pago
    }

    fun resetear() {
        _pagoConfirmado.value = null
    }
}