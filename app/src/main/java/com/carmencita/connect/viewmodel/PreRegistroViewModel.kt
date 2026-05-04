package com.carmencita.connect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carmencita.connect.model.PreRegistro

class PreRegistroViewModel : ViewModel() {

    private val _preRegistro = MutableLiveData<PreRegistro>()
    val preRegistro: LiveData<PreRegistro> = _preRegistro

    private val _guardadoExitoso = MutableLiveData<Boolean>()
    val guardadoExitoso: LiveData<Boolean> = _guardadoExitoso

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun guardarPreRegistro(
        remitente: String,
        destinatario: String,
        destino: String,
        peso: String
    ) {
        if (remitente.isEmpty() || destinatario.isEmpty() || destino.isEmpty()) {
            _error.value = "Completa todos los campos obligatorios"
            return
        }

        val registro = PreRegistro(
            id = (1000..9999).random(),
            remitente = remitente,
            destinatario = destinatario,
            destino = destino,
            peso = peso,
            estado = "pendiente"
        )
        _preRegistro.value = registro
        _guardadoExitoso.value = true
        _error.value = ""
    }
}