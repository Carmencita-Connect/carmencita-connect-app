package com.carmencita.connect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carmencita.connect.data.ChatbotTarifasAssistant
import com.carmencita.connect.model.ChatbotMessage

class ChatbotTarifasViewModel : ViewModel() {

    private val assistant = ChatbotTarifasAssistant()

    private val _mensajes = MutableLiveData<List<ChatbotMessage>>(
        listOf(
            ChatbotMessage(
                "Hola, soy el asistente virtual. Puedo ayudarte con tarifas, horarios, agencias, pagos y tracking. Para cotizar escribe: cotizar largo 30 ancho 20 alto 15 peso 5 origen Trujillo destino Angasmarca",
                enviadoPorUsuario = false
            )
        )
    )
    val mensajes: LiveData<List<ChatbotMessage>> = _mensajes

    fun enviarMensaje(texto: String) {
        val mensaje = texto.trim()
        if (mensaje.isBlank()) return

        val actuales = _mensajes.value.orEmpty()
        val respuesta = assistant.responder(mensaje)
        _mensajes.value = actuales + ChatbotMessage(mensaje, true) +
            ChatbotMessage(respuesta, false)
    }
}
