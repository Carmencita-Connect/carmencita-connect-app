package com.carmencita.connect.data

import android.content.Context
import com.carmencita.connect.model.Sesion

class SesionRepository(context: Context) {

    private val sesionDao = AppDatabase.obtener(context).sesionDao()

    fun obtenerSesion(): Sesion {
        return sesionDao.obtenerActiva()?.toModel() ?: Sesion()
    }

    fun haySesionActiva(): Boolean {
        return sesionDao.obtenerActiva() != null
    }

    fun cerrarSesionLocal() {
        sesionDao.cerrarTodas()
    }
}
