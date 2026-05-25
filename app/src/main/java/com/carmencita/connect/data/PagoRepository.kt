package com.carmencita.connect.data

import com.carmencita.connect.model.Pago
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PagoRepository{

    fun procesarPago(
        metodo: String,
        monto: Double
    ): Pago {
        val numeroPR = "PR-2026-%07d".format((1..9999999).random())
        return Pago(
            id       = (1000..9999).random(),
            numeroPR = numeroPR,
            metodo   = metodo,
            monto    = monto,
            estado   = "confirmado",
            fecha    = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(Date())
        )
    }
}