package com.carmencita.connect.ui.invitado

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.carmencita.connect.databinding.FragmentInvitadoBinding
import com.carmencita.connect.ui.cotizacion.CotizacionFragment
import com.carmencita.connect.R
import com.carmencita.connect.ui.tracking.TrackingFragment

class InvitadoFragment : Fragment() {

    // viewBinding — método del profe
    private var _binding: FragmentInvitadoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // inflar el layout con viewBinding
        _binding = FragmentInvitadoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botón Ver agencias
        binding.btnVerAgencias.setOnClickListener {
            // TODO: navegar a Agencias

            // Botón Tracking
            binding.btnTracking.setOnClickListener {
                // TODO: navegar a Tracking
            }

            // Botón Cotizar
            binding.btnCotizar.setOnClickListener {
                // TODO: navegar a Cotizar
            }

            // Botón Llamar — abre el marcador con el número de Carmencita
            binding.btnLlamar.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:044123456")
                }
                startActivity(intent)
            }
        }

    }
}