package com.carmencita.connect

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Pair
import androidx.activity.enableEdgeToEdge

class Pantalla_Carga : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla_carga)

        // Padding para edge to edge
        val mainView = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Animación del logo
        val logo = findViewById<ImageView>(R.id.logo)
        val animation = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_hacia_abajo)
        logo.startAnimation(animation)

        // Navegar a MainActivity después de 3 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@Pantalla_Carga, MainActivity::class.java)

            val pairs = arrayOf(
                Pair.create(logo as View, "logoTrans")
            )

            val options = ActivityOptions.makeSceneTransitionAnimation(
                this@Pantalla_Carga, *pairs
            )
            startActivity(intent, options.toBundle())
            finish()
        }, 5000) // 5 segundos
    }
}
