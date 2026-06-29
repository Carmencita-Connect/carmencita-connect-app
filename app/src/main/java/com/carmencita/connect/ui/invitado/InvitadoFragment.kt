package com.carmencita.connect.ui.invitado

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.carmencita.connect.R
import com.carmencita.connect.databinding.FragmentInvitadoBinding
import com.carmencita.connect.ui.cotizacion.CotizacionFragment
import com.carmencita.connect.ui.sedes.SedesFragment

class InvitadoFragment : Fragment() {

    private var _binding: FragmentInvitadoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvitadoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnVerAgencias.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, SedesFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnCotizar.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, CotizacionFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnLlamar.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:+51940009748")
            }
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
