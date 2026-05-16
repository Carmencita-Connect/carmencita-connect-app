package com.carmencita.connect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carmencita.connect.data.PreRegistroRepository
import com.carmencita.connect.model.PreRegistro

class PreRegistroViewModel : ViewModel() {

    private val repository = PreRegistroRepository()

    private val _preRegistroGuardado = MutableLiveData<PreRegistro?>()
    val preRegistroGuardado: LiveData<PreRegistro?> = _preRegistroGuardado

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _eliminado = MutableLiveData<Boolean>()


    fun guardarPreRegistro(
        remitente: String,
        destinatario: String,
        metodoPago: String,
        destino: String,
        largo: Double,
        ancho: Double,
        alto: Double,
        peso: Double,
        costoEstimado: Double
    ) {
        // Validar campos vacíos
        if (remitente.isEmpty() || destinatario.isEmpty()) {
            _error.value = "Completa todos los campos"
            return
        }

        // Validar solo letras
        if (!remitente.all { it.isLetter() || it.isWhitespace() }) {
            _error.value = "El remitente solo debe contener letras"
            return
        }

        if (!destinatario.all { it.isLetter() || it.isWhitespace() }) {
            _error.value = "El destinatario solo debe contener letras"
            return
        }

        // Validar método de pago
        if (metodoPago.isEmpty()) {
            _error.value = "Selecciona un método de pago"
            return
        }

        val preRegistro = PreRegistro(
            remitente      = remitente,
            destinatario   = destinatario,
            metodoPago     = metodoPago,
            destino        = destino,
            largo          = largo,
            ancho          = ancho,
            alto           = alto,
            peso           = peso,
            costoEstimado  = costoEstimado
        )

        val guardado = repository.guardar(preRegistro)
        _error.value = ""
        _preRegistroGuardado.value = guardado
    }

    fun resetear() {
        _preRegistroGuardado.value = null
        _error.value = ""
        _eliminado.value = false
    }
}