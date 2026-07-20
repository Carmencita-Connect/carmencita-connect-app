package com.carmencita.connect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carmencita.connect.data.ChatbotTarifasService
import com.carmencita.connect.model.Chatbot

class ChatbotTarifasViewModel : ViewModel() {

    private val chatbotService = ChatbotTarifasService()

    private val _mensajes = MutableLiveData<List<Chatbot>>(
        listOf(
            Chatbot(
                "Hola, soy el asistente virtual. Puedo ayudarte con tarifas, horarios, agencias, pagos y tracking. Para cotizar escribe: cotizar largo 30 ancho 20 alto 15 peso 5 origen Trujillo destino Angasmarca",
                enviadoPorUsuario = false
            )
        )
    )
    val mensajes: LiveData<List<Chatbot>> = _mensajes

    fun enviarMensaje(texto: String) {
        val mensaje = texto.trim()
        if (mensaje.isBlank()) return

        val actuales = _mensajes.value.orEmpty()
        val respuesta = chatbotService.responder(mensaje)
        _mensajes.value = actuales + Chatbot(mensaje, true) +
            Chatbot(respuesta, false)
    }
}
