package com.carmencita.connect.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carmencita.connect.data.ComprobanteRepository

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
        numeroPR: String,
        remitente: String,
        destinatario: String,
        origen: String,
        destino: String,
        costo: Double,
        peso: Double,
        metodoPago: String
    ) {
        _estado.value = ComprobanteEstado.Generando

        // Generar en hilo secundario
        Thread {
            val exito = repository.generarPDF(
                context = context,
                numeroPR = numeroPR,
                remitente = remitente,
                destinatario = destinatario,
                origen = origen,
                destino = destino,
                costo = costo,
                peso = peso,
                metodoPago = metodoPago
            )

            Thread.sleep(2000)

            android.os.Handler(android.os.Looper.getMainLooper()).post {
                if (exito) {
                    _estado.value = ComprobanteEstado.Descargado(numeroPR)
                } else {
                    _estado.value = ComprobanteEstado.Error(
                        "No se pudo generar el comprobante"
                    )
                }
            }
        }.start()
    }

    fun resetear() {
        _estado.value = ComprobanteEstado.Idle
    }
}