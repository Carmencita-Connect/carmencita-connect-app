package com.carmencita.connect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.carmencita.connect.ui.auth.LoginFragment
import com.stripe.android.PaymentConfiguration

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar Stripe
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51TZNAe40wQ1eY6er9ekzm9Z68jDydYtPScUWgrYwkZYyqgXz0CwVtdl7djFfmkkMKFkO6tp30X0knkfvqTa4pKzE00y6wH6Hpu"
        )

        // Carga LoginFragment al iniciar.
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, LoginFragment())
                .commit()
        }
    }
}
