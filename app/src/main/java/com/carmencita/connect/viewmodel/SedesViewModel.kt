package com.carmencita.connect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carmencita.connect.data.SedeRepository
import com.carmencita.connect.model.Sede

class SedesViewModel : ViewModel() {

    private val repository = SedeRepository()

    private val _sedes = MutableLiveData(repository.obtenerSedes())
    val sedes: LiveData<List<Sede>> = _sedes
}
