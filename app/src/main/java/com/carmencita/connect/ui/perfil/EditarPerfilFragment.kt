package com.carmencita.connect.ui.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.databinding.FragmentEditarPerfilBinding
import com.carmencita.connect.viewmodel.PerfilViewModel

class EditarPerfilFragment : Fragment() {

    private var _binding: FragmentEditarPerfilBinding? = null
    private val binding get() = _binding!!

    private val perfilViewModel: PerfilViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditarPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        perfilViewModel.persona.value?.let { persona ->
            binding.etTelefono.setText(persona.telefono)
        }

        binding.btnGuardarCambios.setOnClickListener {
            perfilViewModel.actualizarTelefono(binding.etTelefono.text.toString())
        }

        binding.btnVolver.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        perfilViewModel.persona.observe(viewLifecycleOwner) { persona ->
            if (binding.etTelefono.text.isNullOrBlank()) {
                binding.etTelefono.setText(persona?.telefono.orEmpty())
            }
        }

        perfilViewModel.error.observe(viewLifecycleOwner) { mensaje ->
            binding.tvError.visibility = if (mensaje.isNotBlank()) View.VISIBLE else View.GONE
            binding.tvError.text = mensaje
        }

        perfilViewModel.mensaje.observe(viewLifecycleOwner) { mensaje ->
            binding.tvMensaje.visibility = if (mensaje.isNotBlank()) View.VISIBLE else View.GONE
            binding.tvMensaje.text = mensaje
        }

        perfilViewModel.cargando.observe(viewLifecycleOwner) { cargando ->
            binding.btnGuardarCambios.isEnabled = !cargando
            binding.btnGuardarCambios.text = if (cargando) "Guardando..." else "Guardar cambios"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        perfilViewModel.limpiarMensajes()
        _binding = null
    }
}
