package com.carmencita.connect.ui.preregistro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.R
import com.carmencita.connect.databinding.FragmentPreregistroBinding
import com.carmencita.connect.ui.agenda.AgendaContactosFragment
import com.carmencita.connect.viewmodel.AgendaContactosViewModel
import com.carmencita.connect.viewmodel.CotizacionViewModel
import com.carmencita.connect.viewmodel.PagoViewModel
import com.carmencita.connect.viewmodel.PerfilViewModel
import com.carmencita.connect.viewmodel.PreRegistroViewModel
import com.carmencita.connect.viewmodel.SesionViewModel

class PreRegistroFragment : Fragment(), ConfirmarCancelarDialog.Listener {

    private var _binding: FragmentPreregistroBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PreRegistroViewModel by activityViewModels()
    private val cotizacionViewModel: CotizacionViewModel by activityViewModels()
    private val pagoViewModel: PagoViewModel by activityViewModels()
    private val perfilViewModel: PerfilViewModel by activityViewModels()
    private val sesionViewModel: SesionViewModel by activityViewModels()
    private val agendaViewModel: AgendaContactosViewModel by activityViewModels()
    private var autocompletarSolicitado = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreregistroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sesionViewModel.cargarSesion()

        binding.btnUsarMisDatos.setOnClickListener {
            autocompletarSolicitado = true
            perfilViewModel.cargarPerfil()
        }

        binding.btnSeleccionarDesdeAgenda.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, AgendaContactosFragment.paraSeleccion())
                .addToBackStack(null)
                .commit()
        }

        binding.btnPagoAgencia.setOnClickListener {
            viewModel.seleccionarMetodoPago("agencia")
        }

        binding.btnPagoDigital.setOnClickListener {
            viewModel.seleccionarMetodoPago("digital")
        }

        binding.btnContinuar.setOnClickListener {
            val imm = requireContext().getSystemService(
                android.content.Context.INPUT_METHOD_SERVICE
            ) as android.view.inputmethod.InputMethodManager
            imm.hideSoftInputFromWindow(binding.etRemitente.windowToken, 0)

            val encomienda = cotizacionViewModel.encomiendaCotizada.value

            viewModel.guardarPreRegistro(
                nombreRemitente       = binding.etRemitente.text.toString().trim(),
                dniRemitente          = binding.etDniRemitente.text.toString().trim(),
                telefonoRemitente     = binding.etTelefonoRemitente.text.toString().trim(),
                direccionRemitente    = binding.etDireccionRemitente.text.toString().trim(),
                nombreDestinatario    = binding.etDestinatario.text.toString().trim(),
                dniDestinatario       = binding.etDniDestinatario.text.toString().trim(),
                telefonoDestinatario  = binding.etTelefonoDestinatario.text.toString().trim(),
                direccionDestinatario = binding.etDireccionDestinatario.text.toString().trim(),
                descripcionCarga      = binding.etDescripcionCarga.text.toString().trim(),
                encomienda            = encomienda
            )
        }

        binding.btnEliminar.setOnClickListener {
            ConfirmarCancelarDialog()
                .show(childFragmentManager, "ConfirmarCancelarDialog")
        }

        viewModel.metodoPago.observe(viewLifecycleOwner) { metodo ->
            when (metodo) {
                "agencia" -> {
                    binding.btnPagoAgencia.setBackgroundResource(R.drawable.bg_boton_seleccionado)
                    binding.btnPagoDigital.setBackgroundResource(R.drawable.bg_boton_blanco)
                }
                "digital" -> {
                    binding.btnPagoDigital.setBackgroundResource(R.drawable.bg_boton_seleccionado)
                    binding.btnPagoAgencia.setBackgroundResource(R.drawable.bg_boton_blanco)
                }
                else -> {
                    binding.btnPagoAgencia.setBackgroundResource(R.drawable.bg_boton_blanco)
                    binding.btnPagoDigital.setBackgroundResource(R.drawable.bg_boton_blanco)
                }
            }
        }

        sesionViewModel.sesionActiva.observe(viewLifecycleOwner) { activa ->
            binding.btnUsarMisDatos.visibility = if (activa) View.VISIBLE else View.GONE
            binding.btnSeleccionarDesdeAgenda.visibility = if (activa) View.VISIBLE else View.GONE
        }

        agendaViewModel.contactoSeleccionado.observe(viewLifecycleOwner) { contacto ->
            contacto ?: return@observe
            binding.etDestinatario.setText(contacto.nombre)
            binding.etDniDestinatario.setText(contacto.dni)
            binding.etTelefonoDestinatario.setText(contacto.telefono)
            binding.etDireccionDestinatario.setText(contacto.direccion)
            agendaViewModel.limpiarSeleccion()
        }

        perfilViewModel.persona.observe(viewLifecycleOwner) { persona ->
            persona ?: return@observe
            if (!autocompletarSolicitado) return@observe
            binding.etRemitente.setText(persona.nombre)
            binding.etDniRemitente.setText(persona.dni)
            binding.etTelefonoRemitente.setText(persona.telefono)
            binding.etDireccionRemitente.setText(persona.direccion)
            autocompletarSolicitado = false
        }

        perfilViewModel.error.observe(viewLifecycleOwner) { mensaje ->
            if (mensaje.isNotBlank()) {
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = mensaje
            }
        }

        viewModel.preRegistroGuardado.observe(viewLifecycleOwner) { preRegistro ->
            preRegistro ?: return@observe
            when (viewModel.metodoPago.value) {
                "digital" -> {
                    pagoViewModel.resetear()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.contenedorFragment,
                            com.carmencita.connect.ui.pago.PagoDigitalFragment())
                        .addToBackStack(null)
                        .commit()
                }
                "agencia" -> {
                    pagoViewModel.resetear()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.contenedorFragment,
                            com.carmencita.connect.ui.pago.PagoPresencialFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { mensaje ->
            if (mensaje.isNotEmpty()) {
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = mensaje
            } else {
                binding.tvError.visibility = View.GONE
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : androidx.activity.OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.resetear()
                    cotizacionViewModel.resetear()
                    parentFragmentManager.popBackStack()
                }
            }
        )
    }

    override fun onConfirmarCancelacion() {
        viewModel.resetear()
        cotizacionViewModel.resetear()
        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
