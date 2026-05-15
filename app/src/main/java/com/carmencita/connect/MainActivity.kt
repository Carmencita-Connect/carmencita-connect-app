package com.carmencita.connect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.carmencita.connect.ui.invitado.InvitadoFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Carga InvitadoFragment al iniciar
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, InvitadoFragment())
                .commit()
        }
    }
}
