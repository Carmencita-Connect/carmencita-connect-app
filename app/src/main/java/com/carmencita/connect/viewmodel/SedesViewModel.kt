package com.carmencita.connect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carmencita.connect.model.Sede

class SedesViewModel : ViewModel() {

    private val _sedes = MutableLiveData<List<Sede>>()
    val sedes: LiveData<List<Sede>> = _sedes

    init {
        cargarSedes()
    }

    private fun cargarSedes() {
        // datos reales de Carmencita Express
        _sedes.value = listOf(
            Sede(
                id = 1,
                nombre = "Sede Trujillo",
                direccion = "Av. Principal 123, Trujillo",
                telefono = "044-123456",
                horario = "Lun-Sab 8am - 6pm",
                latitud = -8.1116,
                longitud = -79.0288
            ),
            Sede(
                id = 2,
                nombre = "Sede Angasmarca",
                direccion = "Jr. Comercio 456, Angasmarca",
                telefono = "044-654321",
                horario = "Lun-Sab 8am - 5pm",
                latitud = -8.0394,
                longitud = -78.1597
            )
        )
    }
}
