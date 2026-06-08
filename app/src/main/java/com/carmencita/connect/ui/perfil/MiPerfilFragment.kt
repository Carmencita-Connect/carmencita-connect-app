package com.carmencita.connect.ui.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.R
import com.carmencita.connect.databinding.FragmentMiPerfilBinding
import com.carmencita.connect.ui.auth.LoginFragment
import com.carmencita.connect.viewmodel.PerfilViewModel
import com.carmencita.connect.viewmodel.SesionViewModel

class MiPerfilFragment : Fragment() {

    private var _binding: FragmentMiPerfilBinding? = null
    private val binding get() = _binding!!

    private val perfilViewModel: PerfilViewModel by activityViewModels()
    private val sesionViewModel: SesionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMiPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        perfilViewModel.cargarPerfil()

        binding.btnEditarPerfil.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, EditarPerfilFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnCerrarSesion.setOnClickListener {
            sesionViewModel.cerrarSesion()
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, LoginFragment())
                .commit()
        }

        binding.btnVolver.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        perfilViewModel.persona.observe(viewLifecycleOwner) { persona ->
            binding.tvNombre.text = persona?.nombre.orEmpty()
            binding.tvDni.text = persona?.dni.orEmpty()
            binding.tvTelefono.text = persona?.telefono.orEmpty()
        }

        perfilViewModel.error.observe(viewLifecycleOwner) { mensaje ->
            binding.tvError.visibility = if (mensaje.isNotBlank()) View.VISIBLE else View.GONE
            binding.tvError.text = mensaje
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
