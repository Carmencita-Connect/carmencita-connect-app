package com.carmencita.connect.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carmencita.connect.data.AuthRepository
import com.carmencita.connect.data.auth.RegistroPendiente
import com.carmencita.connect.data.email.EmailJsService
import com.carmencita.connect.data.validation.AuthValidator
import com.carmencita.connect.data.validation.CodigoConfirmacion
import com.carmencita.connect.model.Persona
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistroViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application)
    private val emailService = EmailJsService()

    private var registroPendiente: RegistroPendiente? = null

    private val _codigoEnviado = MutableLiveData(false)
    val codigoEnviado: LiveData<Boolean> = _codigoEnviado

    private val _registroExitoso = MutableLiveData(false)
    val registroExitoso: LiveData<Boolean> = _registroExitoso

    private val _correoDestino = MutableLiveData("")
    val correoDestino: LiveData<String> = _correoDestino

    private val _mensaje = MutableLiveData("")
    val mensaje: LiveData<String> = _mensaje

    private val _error = MutableLiveData("")
    val error: LiveData<String> = _error

    private val _cargando = MutableLiveData(false)
    val cargando: LiveData<Boolean> = _cargando

    fun solicitarCodigo(
        nombre: String,
        dni: String,
        telefono: String,
        correo: String,
        password: String,
        confirmarPassword: String
    ) {
        val validacion = AuthValidator.validarRegistro(
            nombre = nombre,
            dni = dni,
            telefono = telefono,
            correo = correo,
            password = password,
            confirmarPassword = confirmarPassword
        )
        if (validacion.isNotEmpty()) {
            _error.value = validacion
            return
        }

        val persona = Persona(
            nombre = nombre.trim(),
            dni = dni.trim(),
            telefono = telefono.trim(),
            direccion = "",
            correo = correo.trim().lowercase()
        )

        _cargando.value = true
        viewModelScope.launch {
            val resultado = withContext(Dispatchers.IO) {
                val disponibilidad = authRepository.validarDisponibilidad(
                    persona.correo,
                    persona.dni
                )
                if (!disponibilidad.exitoso) {
                    return@withContext disponibilidad.mensaje to null
                }

                val codigo = CodigoConfirmacion.generar()
                val envio = emailService.enviarCodigo(
                    nombre = persona.nombre,
                    correo = persona.correo,
                    codigo = codigo
                )
                if (!envio.exitoso) {
                    return@withContext envio.mensaje to null
                }

                "" to RegistroPendiente(
                    persona = persona,
                    password = password,
                    codigo = codigo,
                    expiraEn = System.currentTimeMillis() + DURACION_CODIGO_MS
                )
            }
            _cargando.value = false

            val error = resultado.first
            val pendiente = resultado.second
            if (pendiente == null) {
                _error.value = error
                return@launch
            }

            registroPendiente = pendiente
            _correoDestino.value = pendiente.persona.correo
            _error.value = ""
            _mensaje.value = "Código enviado a ${pendiente.persona.correo}"
            _codigoEnviado.value = true
        }
    }

    fun confirmarCodigo(codigoIngresado: String) {
        if (!CodigoConfirmacion.esValido(codigoIngresado)) {
            _error.value = "Ingresa el código completo de 6 dígitos"
            return
        }

        val pendiente = registroPendiente
        if (pendiente == null) {
            _error.value = "Solicita un nuevo código de confirmación"
            return
        }
        if (pendiente.estaExpirado()) {
            _error.value = "El código expiró. Solicita uno nuevo"
            return
        }
        if (codigoIngresado != pendiente.codigo) {
            _error.value = "El código ingresado es incorrecto"
            return
        }

        _cargando.value = true
        viewModelScope.launch {
            val resultado = withContext(Dispatchers.IO) {
                authRepository.registrarConfirmado(
                    persona = pendiente.persona,
                    password = pendiente.password
                )
            }
            _cargando.value = false

            if (resultado.exitoso) {
                registroPendiente = null
                _error.value = ""
                _mensaje.value = "Correo confirmado correctamente"
                _registroExitoso.value = true
            } else {
                _error.value = resultado.mensaje
            }
        }
    }

    fun reenviarCodigo() {
        val pendienteActual = registroPendiente
        if (pendienteActual == null) {
            _error.value = "Vuelve al registro para solicitar un código"
            return
        }

        _cargando.value = true
        viewModelScope.launch {
            val nuevoCodigo = CodigoConfirmacion.generar()
            val envio = withContext(Dispatchers.IO) {
                emailService.enviarCodigo(
                    nombre = pendienteActual.persona.nombre,
                    correo = pendienteActual.persona.correo,
                    codigo = nuevoCodigo
                )
            }
            _cargando.value = false

            if (envio.exitoso) {
                registroPendiente = pendienteActual.copy(
                    codigo = nuevoCodigo,
                    expiraEn = System.currentTimeMillis() + DURACION_CODIGO_MS
                )
                _error.value = ""
                _mensaje.value = "Se envió un nuevo código"
            } else {
                _error.value = envio.mensaje
            }
        }
    }

    fun marcarCodigoEnviadoAtendido() {
        _codigoEnviado.value = false
    }

    fun limpiarEstado() {
        registroPendiente = null
        _codigoEnviado.value = false
        _registroExitoso.value = false
        _correoDestino.value = ""
        _mensaje.value = ""
        _error.value = ""
    }

    private companion object {
        const val DURACION_CODIGO_MS = 10 * 60 * 1000L
    }
}
