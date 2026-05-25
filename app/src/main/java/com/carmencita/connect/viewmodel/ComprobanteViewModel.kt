package com.carmencita.connect.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carmencita.connect.data.ComprobanteRepository
import com.carmencita.connect.model.Pago
import com.carmencita.connect.model.PreRegistro

class ComprobanteViewModel : ViewModel() {

    private val repository = ComprobanteRepository()

    sealed class ComprobanteEstado {
        object Idle : ComprobanteEstado()
        object Generando : ComprobanteEstado()
        data class Descargado(val numeroPR: String) : ComprobanteEstado()
        data class Error(val mensaje: String) : ComprobanteEstado()
    }

    private val _estado = MutableLiveData<ComprobanteEstado>(ComprobanteEstado.Idle)
    val estado: LiveData<ComprobanteEstado> = _estado

    fun generarComprobante(
        context: Context,
        pago: Pago,
        preRegistro: PreRegistro
    ) {
        _estado.value = ComprobanteEstado.Generando

        Thread {
            val exito = repository.generarPDF(
                context     = context,
                pago        = pago,
                preRegistro = preRegistro
            )

            Thread.sleep(2000)

            android.os.Handler(android.os.Looper.getMainLooper()).post {
                if (exito) {
                    _estado.value = ComprobanteEstado.Descargado(pago.numeroPR)
                } else {
                    _estado.value = ComprobanteEstado.Error("No se pudo generar el comprobante")
                }
            }
        }.start()
    }

    fun resetear() {
        _estado.value = ComprobanteEstado.Idle
    }
}