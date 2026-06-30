package com.carmencita.connect.data.email

import com.carmencita.connect.BuildConfig
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class EmailJsService {

    data class EmailResult(
        val exitoso: Boolean,
        val mensaje: String = ""
    )

    fun enviarCodigo(nombre: String, correo: String, codigo: String): EmailResult {
        if (!estaConfigurado()) {
            return EmailResult(
                false,
                "Configura EMAILJS_SERVICE_ID, EMAILJS_TEMPLATE_ID y EMAILJS_PUBLIC_KEY"
            )
        }

        val body = JSONObject()
            .put("service_id", BuildConfig.EMAILJS_SERVICE_ID)
            .put("template_id", BuildConfig.EMAILJS_TEMPLATE_ID)
            .put("user_id", BuildConfig.EMAILJS_PUBLIC_KEY)
            .put(
                "template_params",
                JSONObject()
                    .put("to_email", correo)
                    .put("nombre", nombre)
                    .put("codigo", codigo)
                    .put("name", nombre)
                    .put("email", correo)
                    .put("title", "Confirmación de correo | Carmencita Express")
                    .put(
                        "message",
                        "Tu código de confirmación es $codigo. Es válido durante 10 minutos."
                    )
                    .put("marca", "Carmencita Express")
                    .put("color_principal", "#00B3A5")
                    .put("color_secundario", "#235166")
            )
            .toString()

        return runCatching {
            val connection = (
                URL(EMAILJS_SEND_URL).openConnection() as HttpURLConnection
                ).apply {
                requestMethod = "POST"
                connectTimeout = 15_000
                readTimeout = 15_000
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Accept", "application/json")
                doOutput = true
            }

            OutputStreamWriter(connection.outputStream, Charsets.UTF_8).use {
                it.write(body)
            }

            val status = connection.responseCode
            val stream = if (status in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream
            }
            val response = stream?.let {
                BufferedReader(InputStreamReader(it, Charsets.UTF_8)).use { reader ->
                    reader.readText()
                }
            }.orEmpty()
            connection.disconnect()

            if (status in 200..299) {
                EmailResult(true)
            } else {
                EmailResult(false, mensajeError(status, response))
            }
        }.getOrElse {
            EmailResult(false, "No se pudo enviar el código. Revisa tu conexión")
        }
    }

    private fun mensajeError(status: Int, response: String): String {
        val detalle = response.lowercase()
        return when {
            detalle.contains("strict mode") &&
                detalle.contains("private key") ->
                "EmailJS tiene activado Strict Mode. Desactívalo en Account > Security para enviar desde la app Android."

            detalle.contains("non-browser") ||
                detalle.contains("api access") && detalle.contains("disabled") ->
                "EmailJS bloqueó el envío desde Android. Activa el acceso para aplicaciones no navegador en Account > Security."

            status == HttpURLConnection.HTTP_UNAUTHORIZED ||
                status == HttpURLConnection.HTTP_FORBIDDEN ->
                "EmailJS rechazó las credenciales. Revisa Service ID, Template ID y Public Key."

            detalle.contains("template") ->
                "No se encontró la plantilla de confirmación configurada en EmailJS."

            detalle.contains("service") ->
                "No se encontró el servicio de correo configurado en EmailJS."

            else -> "No se pudo enviar el código de confirmación. Intenta nuevamente."
        }
    }

    private fun estaConfigurado(): Boolean {
        return BuildConfig.EMAILJS_SERVICE_ID.isNotBlank() &&
            BuildConfig.EMAILJS_TEMPLATE_ID.isNotBlank() &&
            BuildConfig.EMAILJS_PUBLIC_KEY.isNotBlank()
    }

    private companion object {
        const val EMAILJS_SEND_URL = "https://api.emailjs.com/api/v1.0/email/send"
    }
}
