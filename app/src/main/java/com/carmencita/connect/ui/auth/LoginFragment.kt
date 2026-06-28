package com.carmencita.connect.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.R
import com.carmencita.connect.databinding.FragmentLoginBinding
import com.carmencita.connect.ui.invitado.InvitadoFragment
import com.carmencita.connect.viewmodel.LoginViewModel
import com.carmencita.connect.viewmodel.PerfilViewModel
import com.carmencita.connect.viewmodel.SesionViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by activityViewModels()
    private val sesionViewModel: SesionViewModel by activityViewModels()
    private val perfilViewModel: PerfilViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnIniciarSesion.setOnClickListener {
            loginViewModel.iniciarSesion(
                correo = binding.etCorreo.text.toString(),
                password = binding.etPassword.text.toString()
            )
        }

        binding.tvRegistrate.setOnClickListener {
            loginViewModel.limpiarEstado()
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, RegistroFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnInvitado.setOnClickListener {
            sesionViewModel.entrarComoInvitado()
            perfilViewModel.limpiarPerfil()
            navegarInicio()
        }

        loginViewModel.loginExitoso.observe(viewLifecycleOwner) { exitoso ->
            if (exitoso) {
                sesionViewModel.cargarSesion()
                loginViewModel.limpiarEstado()
                navegarInicio()
            }
        }

        loginViewModel.error.observe(viewLifecycleOwner) { mensaje ->
            mostrarError(mensaje)
        }

        loginViewModel.cargando.observe(viewLifecycleOwner) { cargando ->
            binding.btnIniciarSesion.isEnabled = !cargando
            binding.btnInvitado.isEnabled = !cargando
            binding.btnIniciarSesion.text = if (cargando) "Validando..." else "Inicia sesión"
        }
    }

    private fun navegarInicio() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.contenedorFragment, InvitadoFragment())
            .commit()
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
