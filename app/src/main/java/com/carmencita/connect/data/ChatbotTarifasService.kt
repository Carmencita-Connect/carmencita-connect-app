package com.carmencita.connect.data

import java.text.Normalizer
import java.util.Locale

class ChatbotTarifasService(
    private val tarifaRepository: TarifaRepository = TarifaRepository()
) {

    fun responder(mensajeUsuario: String): String {
        val mensaje = mensajeUsuario.trim()
        if (mensaje.isBlank()) return "Escribe una consulta para poder ayudarte."

        val normalizado = normalizar(mensaje)
        val solicitudCotizacion = extraerSolicitudCotizacion(mensaje)

        if (solicitudCotizacion != null) {
            return calcularCotizacion(solicitudCotizacion)
        }

        return when {
            contiene(normalizado, "tarifa", "costo", "cuanto", "cotizar") -> FORMATO_COTIZACION
            contiene(normalizado, "tracking", "rastreo", "rastrear", "guia", "seguir") ->
                "Para rastrear tu encomienda ingresa a Tracking y escribe tu numero de guia."
            contiene(normalizado, "llamar", "llamo", "telefono", "contacto") ->
                "Para comunicarte con una sede entra a Ver agencias y presiona Llamar sede."
            contiene(normalizado, "prohibido", "restringido", "peligroso", "articulo", "articulos", "puedo enviar") ->
                "No se deben enviar articulos peligrosos, ilegales, inflamables, dinero en efectivo o productos no permitidos."
            contiene(normalizado, "horario", "atienden", "atencion") ->
                "Nuestro horario referencial de atencion es Lun - Dom de 8:00 am a 6:00 pm."
            contiene(normalizado, "agencia", "sede", "ubicacion", "direccion") ->
                "Puedes revisar las agencias en Ver agencias. Tenemos sedes en Trujillo, Angasmarca y Santiago de Surco."
            contiene(normalizado, "pago", "pagar", "yape", "tarjeta", "online") ->
                "Puedes pagar en agencia o elegir pago digital durante el pre-registro."
            contiene(normalizado, "dato", "datos", "encomienda", "enviar") ->
                "Para enviar una encomienda necesitas nombre, DNI, telefono y direccion del remitente y destinatario, ademas de la descripcion de la carga."
            else ->
                "Puedo ayudarte con tarifas, horarios, agencias, pagos, tracking, llamadas a sedes y articulos restringidos. Para cotizar usa: $EJEMPLO_COTIZACION"
        }
    }

    private fun calcularCotizacion(solicitud: SolicitudCotizacion): String {
        val error = validar(solicitud)
        if (error != null) return error

        val tarifa = tarifaRepository.calcularTarifa(
            largo = solicitud.largo,
            ancho = solicitud.ancho,
            alto = solicitud.alto,
            peso = solicitud.peso,
            destino = solicitud.destino
        )

        return "La tarifa estimada para enviar de ${solicitud.origen} a ${solicitud.destino} es S/ %.2f. Este monto es referencial y puede confirmarse en el pre-registro.".format(tarifa.costo)
    }

    private fun validar(solicitud: SolicitudCotizacion): String? {
        if (solicitud.largo <= 0 || solicitud.ancho <= 0 || solicitud.alto <= 0) {
            return "Las medidas deben ser numeros positivos."
        }
        if (solicitud.peso <= 0) return "El peso debe ser un numero positivo."
        if (solicitud.origen.equals(solicitud.destino, ignoreCase = true)) {
            return "El origen y destino no pueden ser iguales."
        }
        return null
    }

    private fun extraerSolicitudCotizacion(mensaje: String): SolicitudCotizacion? {
        val largo = extraerNumero(mensaje, "largo") ?: return null
        val ancho = extraerNumero(mensaje, "ancho") ?: return null
        val alto = extraerNumero(mensaje, "alto") ?: return null
        val peso = extraerNumero(mensaje, "peso") ?: return null
        val origen = extraerTexto(mensaje, "origen", "destino") ?: return null
        val destino = extraerTexto(mensaje, "destino") ?: return null

        return SolicitudCotizacion(
            largo = largo,
            ancho = ancho,
            alto = alto,
            peso = peso,
            origen = origen,
            destino = destino
        )
    }

    private fun extraerNumero(mensaje: String, etiqueta: String): Double? {
        val regex = Regex(
            "$etiqueta\\s*[:=]?\\s*(\\d+(?:[.,]\\d+)?)",
            setOf(RegexOption.IGNORE_CASE)
        )
        return regex.find(mensaje)
            ?.groupValues
            ?.getOrNull(1)
            ?.replace(',', '.')
            ?.toDoubleOrNull()
    }

    private fun extraerTexto(mensaje: String, etiqueta: String, hasta: String? = null): String? {
        val fin = hasta?.let { "\\s+$it\\b" } ?: "$"
        val regex = Regex(
            "$etiqueta\\s*[:=]?\\s*([A-Za-zÀ-ÿ ]+?)(?=$fin|$)",
            setOf(RegexOption.IGNORE_CASE)
        )
        return regex.find(mensaje)
            ?.groupValues
            ?.getOrNull(1)
            ?.trim()
            ?.takeIf { it.isNotBlank() }
    }

    private fun contiene(mensajeNormalizado: String, vararg palabras: String): Boolean {
        return palabras.any { mensajeNormalizado.contains(it) }
    }

    private fun normalizar(texto: String): String {
        val sinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replace("\\p{Mn}+".toRegex(), "")
        return sinTildes.lowercase(Locale.getDefault())
    }

    private data class SolicitudCotizacion(
        val largo: Double,
        val ancho: Double,
        val alto: Double,
        val peso: Double,
        val origen: String,
        val destino: String
    )

    private companion object {
        const val EJEMPLO_COTIZACION =
            "cotizar largo 30 ancho 20 alto 15 peso 5 origen Trujillo destino Angasmarca"
        const val FORMATO_COTIZACION =
            "Para calcular una tarifa escribe: $EJEMPLO_COTIZACION"
    }
}
