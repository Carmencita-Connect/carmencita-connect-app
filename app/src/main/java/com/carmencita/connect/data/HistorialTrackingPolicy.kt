package com.carmencita.connect.data

object HistorialTrackingPolicy {

    const val LIMITE_CODIGOS = 5

    fun agregar(codigosActuales: List<String>, nuevoCodigo: String): List<String> {
        val codigoNormalizado = nuevoCodigo.trim().uppercase()
        if (codigoNormalizado.isBlank()) return codigosActuales

        return buildList {
            add(codigoNormalizado)
            addAll(
                codigosActuales.filterNot {
                    it.equals(codigoNormalizado, ignoreCase = true)
                }
            )
        }.take(LIMITE_CODIGOS)
    }
}
