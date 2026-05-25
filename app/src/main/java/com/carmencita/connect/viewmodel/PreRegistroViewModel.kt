package com.carmencita.connect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carmencita.connect.data.PreRegistroRepository
import com.carmencita.connect.model.Encomienda
import com.carmencita.connect.model.Persona
import com.carmencita.connect.model.PreRegistro

class PreRegistroViewModel : ViewModel() {

    private val repository = PreRegistroRepository()

    private val _preRegistroGuardado = MutableLiveData<PreRegistro?>()
    val preRegistroGuardado: LiveData<PreRegistro?> = _preRegistroGuardado

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _metodoPago = MutableLiveData<String>("")
    val metodoPago: LiveData<String> = _metodoPago

    fun seleccionarMetodoPago(metodo: String) {
        _metodoPago.value = metodo
    }

    fun limpiarPreRegistroGuardado() {
        _preRegistroGuardado.value = null
    }

    fun guardarPreRegistro(
        nombreRemitente: String,
        dniRemitente: String,
        telefonoRemitente: String,
        direccionRemitente: String,
        nombreDestinatario: String,
        dniDestinatario: String,
        telefonoDestinatario: String,
        direccionDestinatario: String,
        descripcionCarga: String,
        encomienda: Encomienda?
    ) {
        if (nombreRemitente.isEmpty() || nombreDestinatario.isEmpty()) {
            _error.value = "Completa el nombre del remitente y destinatario"
            return
        }
        if (dniRemitente.isEmpty() || dniDestinatario.isEmpty()) {
            _error.value = "Completa el DNI del remitente y destinatario"
            return
        }
        if (telefonoRemitente.isEmpty() || telefonoDestinatario.isEmpty()) {
            _error.value = "Completa el teléfono del remitente y destinatario"
            return
        }
        if (direccionRemitente.isEmpty() || direccionDestinatario.isEmpty()) {
            _error.value = "Completa la dirección del remitente y destinatario"
            return
        }
        if (descripcionCarga.isEmpty()) {
            _error.value = "Ingresa una descripción de la carga"
            return
        }
        if (!nombreRemitente.all { it.isLetter() || it.isWhitespace() }) {
            _error.value = "El nombre del remitente solo debe contener letras"
            return
        }
        if (!nombreDestinatario.all { it.isLetter() || it.isWhitespace() }) {
            _error.value = "El nombre del destinatario solo debe contener letras"
            return
        }
        if (dniRemitente.length != 8 || !dniRemitente.all { it.isDigit() }) {
            _error.value = "El DNI del remitente debe tener 8 dígitos"
            return
        }
        if (dniDestinatario.length != 8 || !dniDestinatario.all { it.isDigit() }) {
            _error.value = "El DNI del destinatario debe tener 8 dígitos"
            return
        }
        if (telefonoRemitente.length != 9 || !telefonoRemitente.all { it.isDigit() }) {
            _error.value = "El teléfono del remitente debe tener 9 dígitos"
            return
        }
        if (telefonoDestinatario.length != 9 || !telefonoDestinatario.all { it.isDigit() }) {
            _error.value = "El teléfono del destinatario debe tener 9 dígitos"
            return
        }
        val metodo = _metodoPago.value ?: ""
        if (metodo.isEmpty()) {
            _error.value = "Selecciona un modo de pago"
            return
        }
        if (encomienda == null ||
            encomienda.largo <= 0 ||
            encomienda.ancho <= 0 ||
            encomienda.alto <= 0 ||
            encomienda.peso <= 0
        ) {
            _error.value = "Primero realiza una cotizacion valida"
            return
        }

        val remitente = Persona(
            nombre    = nombreRemitente,
            dni       = dniRemitente,
            telefono  = telefonoRemitente,
            direccion = direccionRemitente
        )

        val destinatario = Persona(
            nombre    = nombreDestinatario,
            dni       = dniDestinatario,
            telefono  = telefonoDestinatario,
            direccion = direccionDestinatario
        )

        val guardado = repository.guardar(
            remitente        = remitente,
            destinatario     = destinatario,
            descripcionCarga = descripcionCarga,
            encomienda       = encomienda
        )
        _error.value = ""
        _preRegistroGuardado.value = guardado
    }

    fun marcarComoPagado() {
        val actual = _preRegistroGuardado.value ?: return
        _preRegistroGuardado.value = actual.copy(estado = "pagado")
    }

    fun resetear() {
        _preRegistroGuardado.value = null
        _metodoPago.value = ""
        _error.value = ""
    }
}
