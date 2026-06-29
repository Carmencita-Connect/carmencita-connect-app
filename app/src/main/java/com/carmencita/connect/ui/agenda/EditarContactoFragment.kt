package com.carmencita.connect.ui.agenda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.databinding.FragmentEditarContactoBinding
import com.carmencita.connect.viewmodel.AgendaContactosViewModel

class EditarContactoFragment : Fragment() {

    private var _binding: FragmentEditarContactoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AgendaContactosViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditarContactoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.limpiarMensajes()

        viewModel.contactoEnEdicion.value?.let { contacto ->
            binding.etTelefonoContacto.setText(contacto.telefono)
        }

        binding.btnGuardarCambiosContacto.setOnClickListener {
            viewModel.actualizarTelefonoContacto(binding.etTelefonoContacto.text.toString())
        }

        binding.btnCancelarEdicion.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewModel.error.observe(viewLifecycleOwner) { mensaje ->
            binding.tvErrorEditarContacto.visibility = if (mensaje.isBlank()) View.GONE else View.VISIBLE
            binding.tvErrorEditarContacto.text = mensaje
        }

        viewModel.mensaje.observe(viewLifecycleOwner) { mensaje ->
            if (mensaje.isNotBlank()) {
                parentFragmentManager.popBackStack()
                viewModel.limpiarMensajes()
            }
        }

        viewModel.cargando.observe(viewLifecycleOwner) { cargando ->
            binding.btnGuardarCambiosContacto.isEnabled = !cargando
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
