package com.carmencita.connect.ui.menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.R
import com.carmencita.connect.databinding.FragmentMenuPrincipalBinding
import com.carmencita.connect.ui.agenda.AgendaContactosFragment
import com.carmencita.connect.ui.auth.LoginFragment
import com.carmencita.connect.ui.cotizacion.CotizacionFragment
import com.carmencita.connect.ui.notificaciones.HistorialNotificacionesFragment
import com.carmencita.connect.ui.perfil.MiPerfilFragment
import com.carmencita.connect.ui.sedes.SedesFragment
import com.carmencita.connect.ui.tracking.TrackingFragment
import com.carmencita.connect.viewmodel.PerfilViewModel
import com.carmencita.connect.viewmodel.SesionViewModel

class MenuPrincipalFragment : Fragment() {

    private var _binding: FragmentMenuPrincipalBinding? = null
    private val binding get() = _binding!!

    private val sesionViewModel: SesionViewModel by activityViewModels()
    private val perfilViewModel: PerfilViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuPrincipalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sesionViewModel.cargarSesion()
        perfilViewModel.cargarPerfil()

        binding.btnMiPerfil.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, MiPerfilFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnAgendaContactos.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, AgendaContactosFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnHistorialNotificaciones.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, HistorialNotificacionesFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnVerAgencias.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, SedesFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnTracking.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, TrackingFragment())
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

        binding.btnCerrarSesion.setOnClickListener {
            sesionViewModel.cerrarSesion()
            perfilViewModel.limpiarPerfil()
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, LoginFragment())
                .commit()
        }

        perfilViewModel.persona.observe(viewLifecycleOwner) { persona ->
            val nombre = persona?.nombre.orEmpty().trim()
            binding.tvTituloInicio.text = if (nombre.isBlank()) {
                getString(R.string.titulo_menu_principal)
            } else {
                "Hola, $nombre"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
