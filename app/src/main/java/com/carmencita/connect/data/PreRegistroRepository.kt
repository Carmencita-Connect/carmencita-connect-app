package com.carmencita.connect.data

import com.carmencita.connect.model.Encomienda
import com.carmencita.connect.model.Persona
import com.carmencita.connect.model.PreRegistro
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PreRegistroRepository {

    private val preRegistros = mutableListOf<PreRegistro>()

    fun guardar(
        remitente: Persona,
        destinatario: Persona,
        descripcionCarga: String,
        encomienda: Encomienda
    ): PreRegistro {
        val nuevo = PreRegistro(
            id               = (1000..9999).random(),
            remitente        = remitente,
            destinatario     = destinatario,
            descripcionCarga = descripcionCarga,
            encomienda       = encomienda,
            estado           = "pendiente",
            fechaCreacion    = SimpleDateFormat(
                "dd/MM/yy", Locale.getDefault()
            ).format(Date())
        )
        preRegistros.add(nuevo)
        return nuevo
    }
}
