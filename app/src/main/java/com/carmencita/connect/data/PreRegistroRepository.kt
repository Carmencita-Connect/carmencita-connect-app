package com.carmencita.connect.data

import com.carmencita.connect.model.PreRegistro

class PreRegistroRepository {

    // Lista temporal en memoria
    private val preRegistros = mutableListOf<PreRegistro>()

    fun guardar(preRegistro: PreRegistro): PreRegistro {
        val nuevo = preRegistro.copy(
            id = (1000..9999).random(),
            fechaCreacion = java.text.SimpleDateFormat(
                "dd/MM/yy", java.util.Locale.getDefault()
            ).format(java.util.Date())
        )
        preRegistros.add(nuevo)
        return nuevo
    }

    fun eliminar(id: Int) {
        preRegistros.removeAll { it.id == id }
    }

    fun obtenerTodos(): List<PreRegistro> {
        return preRegistros.toList()
    }
}