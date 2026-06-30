package com.carmencita.connect.ui.agenda

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.R
import com.carmencita.connect.databinding.FragmentAgendaContactosBinding
import com.carmencita.connect.databinding.ItemContactoFrecuenteBinding
import com.carmencita.connect.model.Persona
import com.carmencita.connect.viewmodel.AgendaContactosViewModel

class AgendaContactosFragment : Fragment(), EliminarContactoDialog.Listener {

    private var _binding: FragmentAgendaContactosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AgendaContactosViewModel by activityViewModels()
    private val modoSeleccion: Boolean by lazy {
        arguments?.getBoolean(ARG_MODO_SELECCION, false) ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgendaContactosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.cargarContactos()

        if (modoSeleccion) {
            binding.tvSubtituloAgenda.text = "Elige un destinatario frecuente"
            binding.btnAgregarContacto.visibility = View.GONE
        }

        binding.btnAgregarContacto.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, NuevoContactoFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.etBuscarContacto.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.buscar(s?.toString().orEmpty())
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })

        viewModel.contactos.observe(viewLifecycleOwner, ::mostrarContactos)
        viewModel.error.observe(viewLifecycleOwner) { mostrarTexto(binding.tvErrorAgenda, it) }
        viewModel.mensaje.observe(viewLifecycleOwner) { mostrarTexto(binding.tvMensajeAgenda, it) }
    }

    private fun mostrarContactos(contactos: List<Persona>) {
        binding.contenedorContactos.removeAllViews()
        binding.tvAgendaVacia.visibility = if (contactos.isEmpty()) View.VISIBLE else View.GONE

        contactos.forEach { contacto ->
            val item = ItemContactoFrecuenteBinding.inflate(
                layoutInflater,
                binding.contenedorContactos,
                false
            )
            item.tvNombreContacto.text = contacto.nombre
            item.tvDniContacto.text = "DNI: ${contacto.dni}"
            item.tvTelefonoContacto.text = "Telefono: ${contacto.telefono}"
            item.tvDireccionContacto.text = "Direccion: ${contacto.direccion}"
            item.contenedorAcciones.visibility = if (modoSeleccion) View.GONE else View.VISIBLE

            item.contenedorContacto.setOnClickListener {
                if (modoSeleccion) {
                    viewModel.seleccionar(contacto)
                    parentFragmentManager.popBackStack()
                }
            }
            item.btnEditarContacto.setOnClickListener {
                viewModel.prepararEdicion(contacto)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.contenedorFragment, EditarContactoFragment())
                    .addToBackStack(null)
                    .commit()
            }
            item.btnEliminarContacto.setOnClickListener {
                EliminarContactoDialog
                    .newInstance(contacto.dni, contacto.nombre)
                    .show(childFragmentManager, "EliminarContactoDialog")
            }
            binding.contenedorContactos.addView(item.root)
        }
    }

    override fun onConfirmarEliminarContacto(dni: String) {
        val contacto = viewModel.contactos.value.orEmpty().firstOrNull { it.dni == dni }
            ?: return
        viewModel.eliminar(contacto)
    }

    private fun mostrarTexto(textView: TextView, mensaje: String) {
        textView.visibility = if (mensaje.isBlank()) View.GONE else View.VISIBLE
        textView.text = mensaje
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_MODO_SELECCION = "modo_seleccion"

        fun paraSeleccion() = AgendaContactosFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_MODO_SELECCION, true)
            }
        }
    }
}
