package com.carmencita.connect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TrackingViewModel : ViewModel() {

    private val _encomienda = MutableLiveData<String>()
    val encomienda: LiveData<String> = _encomienda

    fun buscarEncomienda(numeroGuia: String) {
        // Sprint 1 - A
    }
}