package com.carmencita.connect.ui.agenda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.databinding.FragmentNuevoContactoBinding
import com.carmencita.connect.viewmodel.AgendaContactosViewModel

class NuevoContactoFragment : Fragment() {

    private var _binding: FragmentNuevoContactoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AgendaContactosViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNuevoContactoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.limpiarMensajes()

        binding.btnGuardarContacto.setOnClickListener {
            viewModel.guardar(
                nombre = binding.etNombreContacto.text.toString(),
                dni = binding.etDniContacto.text.toString(),
                telefono = binding.etTelefonoContacto.text.toString(),
                direccion = binding.etDireccionContacto.text.toString()
            )
        }

        binding.btnCancelarContacto.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewModel.error.observe(viewLifecycleOwner) { mensaje ->
            binding.tvErrorNuevoContacto.visibility = if (mensaje.isBlank()) View.GONE else View.VISIBLE
            binding.tvErrorNuevoContacto.text = mensaje
        }

        viewModel.mensaje.observe(viewLifecycleOwner) { mensaje ->
            if (mensaje.isNotBlank()) {
                parentFragmentManager.popBackStack()
                viewModel.limpiarMensajes()
            }
        }

        viewModel.cargando.observe(viewLifecycleOwner) { cargando ->
            binding.btnGuardarContacto.isEnabled = !cargando
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
