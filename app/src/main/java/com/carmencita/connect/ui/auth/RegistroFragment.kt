package com.carmencita.connect.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.R
import com.carmencita.connect.databinding.FragmentRegistroBinding
import com.carmencita.connect.viewmodel.RegistroViewModel

class RegistroFragment : Fragment() {

    private var _binding: FragmentRegistroBinding? = null
    private val binding get() = _binding!!

    private val registroViewModel: RegistroViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegistrarse.setOnClickListener {
            registroViewModel.solicitarCodigo(
                nombre = binding.etNombre.text.toString(),
                dni = binding.etDni.text.toString(),
                telefono = binding.etTelefono.text.toString(),
                correo = binding.etCorreo.text.toString(),
                password = binding.etPassword.text.toString(),
                confirmarPassword = binding.etConfirmarPassword.text.toString()
            )
        }

        binding.tvIniciaSesion.setOnClickListener {
            registroViewModel.limpiarEstado()
            parentFragmentManager.popBackStack()
        }

        registroViewModel.codigoEnviado.observe(viewLifecycleOwner) { enviado ->
            if (enviado) {
                registroViewModel.marcarCodigoEnviadoAtendido()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.contenedorFragment, ConfirmacionCorreoFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        registroViewModel.error.observe(viewLifecycleOwner) { mensaje ->
            mostrarError(mensaje)
        }

        registroViewModel.cargando.observe(viewLifecycleOwner) { cargando ->
            binding.btnRegistrarse.isEnabled = !cargando
            binding.tvIniciaSesion.isEnabled = !cargando
            binding.btnRegistrarse.text = if (cargando) "Enviando código..." else "Registrarte"
        }
    }

    private fun mostrarError(mensaje: String) {
        binding.tvError.visibility = if (mensaje.isNotBlank()) View.VISIBLE else View.GONE
        binding.tvError.text = mensaje
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
