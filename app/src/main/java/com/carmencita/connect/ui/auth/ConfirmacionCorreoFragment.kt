package com.carmencita.connect.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.R
import com.carmencita.connect.databinding.FragmentConfirmacionCorreoBinding
import com.carmencita.connect.ui.menu.MenuPrincipalFragment
import com.carmencita.connect.viewmodel.RegistroViewModel
import com.carmencita.connect.viewmodel.SesionViewModel

class ConfirmacionCorreoFragment : Fragment() {

    private var _binding: FragmentConfirmacionCorreoBinding? = null
    private val binding get() = _binding!!

    private val registroViewModel: RegistroViewModel by activityViewModels()
    private val sesionViewModel: SesionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmacionCorreoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val campos = listOf(
            binding.etCodigo1,
            binding.etCodigo2,
            binding.etCodigo3,
            binding.etCodigo4,
            binding.etCodigo5,
            binding.etCodigo6
        )
        configurarCamposCodigo(campos)
        campos.first().requestFocus()

        binding.btnConfirmarCodigo.setOnClickListener {
            registroViewModel.confirmarCodigo(campos.joinToString("") { it.text.toString() })
        }
        binding.tvReenviarCodigo.setOnClickListener {
            registroViewModel.reenviarCodigo()
        }
        binding.tvVolverRegistro.setOnClickListener {
            registroViewModel.limpiarEstado()
            parentFragmentManager.popBackStack()
        }

        registroViewModel.correoDestino.observe(viewLifecycleOwner) { correo ->
            binding.tvCorreoDestino.text = if (correo.isBlank()) {
                ""
            } else {
                "Enviamos un código a $correo"
            }
        }
        registroViewModel.error.observe(viewLifecycleOwner) { mensaje ->
            binding.tvError.visibility = if (mensaje.isBlank()) View.GONE else View.VISIBLE
            binding.tvError.text = mensaje
        }
        registroViewModel.mensaje.observe(viewLifecycleOwner) { mensaje ->
            binding.tvMensaje.visibility = if (mensaje.isBlank()) View.GONE else View.VISIBLE
            binding.tvMensaje.text = mensaje
        }
        registroViewModel.cargando.observe(viewLifecycleOwner) { cargando ->
            binding.btnConfirmarCodigo.isEnabled = !cargando
            binding.tvReenviarCodigo.isEnabled = !cargando
            binding.btnConfirmarCodigo.text =
                if (cargando) "Verificando..." else "Confirmar código"
        }
        registroViewModel.registroExitoso.observe(viewLifecycleOwner) { exitoso ->
            if (exitoso) {
                sesionViewModel.cargarSesion()
                registroViewModel.limpiarEstado()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.contenedorFragment, MenuPrincipalFragment())
                    .commit()
            }
        }
    }

    private fun configurarCamposCodigo(campos: List<EditText>) {
        campos.forEachIndexed { index, campo ->
            campo.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

                override fun afterTextChanged(editable: Editable?) {
                    if (editable?.length == 1 && index < campos.lastIndex) {
                        campos[index + 1].requestFocus()
                    }
                }
            })
            campo.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL &&
                    event.action == KeyEvent.ACTION_DOWN &&
                    campo.text.isEmpty() &&
                    index > 0
                ) {
                    campos[index - 1].requestFocus()
                    campos[index - 1].text.clear()
                    true
                } else {
                    false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
