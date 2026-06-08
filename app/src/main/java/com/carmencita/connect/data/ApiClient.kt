package com.carmencita.connect.data

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object ApiClient {

    private const val BASE_URL = "http://10.0.2.2:8080"

    data class ApiResponse(
        val statusCode: Int,
        val body: String
    ) {
        val isSuccessful: Boolean get() = statusCode in 200..299
    }

    fun post(path: String, body: String, token: String? = null): ApiResponse {
        return request("POST", path, body, token)
    }

    fun patch(path: String, body: String, token: String? = null): ApiResponse {
        return request("PATCH", path, body, token)
    }

    fun get(path: String, token: String? = null): ApiResponse {
        return request("GET", path, null, token)
    }

    private fun request(
        method: String,
        path: String,
        body: String?,
        token: String?
    ): ApiResponse {
        val connection = (URL(BASE_URL + path).openConnection() as HttpURLConnection).apply {
            requestMethod = method
            connectTimeout = 15000
            readTimeout = 15000
            setRequestProperty("Accept", "application/json")
            setRequestProperty("Content-Type", "application/json")
            token?.takeIf { it.isNotBlank() }?.let {
                setRequestProperty("Authorization", "Bearer $it")
            }
            doInput = true
            if (body != null) {
                doOutput = true
            }
        }

        if (body != null) {
            OutputStreamWriter(connection.outputStream, Charsets.UTF_8).use { writer ->
                writer.write(body)
            }
        }

        val statusCode = connection.responseCode
        val stream = if (statusCode in 200..299) {
            connection.inputStream
        } else {
            connection.errorStream ?: connection.inputStream
        }

        val responseBody = BufferedReader(InputStreamReader(stream, Charsets.UTF_8)).use { reader ->
            reader.readText()
        }
        connection.disconnect()

        return ApiResponse(statusCode, responseBody)
    }
}
