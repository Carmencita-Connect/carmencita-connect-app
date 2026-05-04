package com.carmencita.connect.ui.sedes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.carmencita.connect.R
import com.carmencita.connect.model.Sede
import com.carmencita.connect.viewmodel.SedesViewModel

class SedesFragment : Fragment() {

    private val viewModel: SedesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sedes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contenedor = view.findViewById<LinearLayout>(R.id.contenedorSedes)

        viewModel.sedes.observe(viewLifecycleOwner) { listaSedes ->
            contenedor.removeAllViews()
            listaSedes.forEach { sede ->
                val cardSede = crearCardSede(sede)
                contenedor.addView(cardSede)
            }
        }
    }

    private fun crearCardSede(sede: Sede): View {
        val card = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
            setBackgroundResource(android.R.color.white)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, 24) }
        }

        val tvNombre = TextView(requireContext()).apply {
            text = sede.nombre
            textSize = 16f
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        val tvDireccion = TextView(requireContext()).apply {
            text = sede.direccion
            textSize = 14f
        }

        val tvHorario = TextView(requireContext()).apply {
            text = sede.horario
            textSize = 14f
        }

        val tvTelefono = TextView(requireContext()).apply {
            text = sede.telefono
            textSize = 14f
            setTextColor(android.graphics.Color.parseColor("#1565C0"))
            setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${sede.telefono}")
                }
                startActivity(intent)
            }
        }

        card.addView(tvNombre)
        card.addView(tvDireccion)
        card.addView(tvHorario)
        card.addView(tvTelefono)

        return card
    }
}