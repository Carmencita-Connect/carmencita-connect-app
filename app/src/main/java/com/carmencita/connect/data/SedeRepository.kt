package com.carmencita.connect.data

import com.carmencita.connect.model.Sede

class SedeRepository {

    fun obtenerSedes(): List<Sede> = listOf(
        Sede(
            id = 1,
            nombre = "Agencia Trujillo",
            direccion = "Av. America Sur 222",
            telefono = "(044) 123456",
            horario = "Lun - Dom: 8:00 am - 6:00 pm",
            latitud = -8.1116,
            longitud = -79.0287
        ),
        Sede(
            id = 2,
            nombre = "Agencia Angasmarca",
            direccion = "Jr. Comercio 120",
            telefono = "(044) 654321",
            horario = "Lun - Dom: 8:00 am - 6:00 pm",
            latitud = -8.1322,
            longitud = -78.0556
        ),
        Sede(
            id = 3,
            nombre = "Agencia Santiago de Surco",
            direccion = "Av. Caminos del Inca 450",
            telefono = "(01) 4567890",
            horario = "Lun - Dom: 8:00 am - 6:00 pm",
            latitud = -12.1450,
            longitud = -76.9916
        )
    )
}
