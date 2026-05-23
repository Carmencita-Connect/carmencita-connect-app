package com.carmencita.connect.ui.pago

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.R
import com.carmencita.connect.databinding.FragmentPagoConfirmadoBinding
import com.carmencita.connect.viewmodel.ComprobanteViewModel
import com.carmencita.connect.viewmodel.PagoViewModel
import com.carmencita.connect.viewmodel.CotizacionViewModel
import com.carmencita.connect.viewmodel.PreRegistroViewModel

class PagoConfirmadoFragment : Fragment() {

    private var _binding: FragmentPagoConfirmadoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PagoViewModel by activityViewModels()
    private val comprobanteViewModel: ComprobanteViewModel by activityViewModels()
    private val cotizacionViewModel: CotizacionViewModel by activityViewModels()
    private val preRegistroViewModel: PreRegistroViewModel by activityViewModels()

    companion object {
        fun newInstance(numeroPR: String) = PagoConfirmadoFragment().apply {
            arguments = Bundle().apply {
                putString("numeroPR", numeroPR)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagoConfirmadoBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mostrar número de pre-registro
        arguments?.getString("numeroPR")?.let { numero ->
            binding.tvNumeroPreRegistro.text = numero
        }

        // Botón Inicio — vuelve al InvitadoFragment
        binding.btnInicio.setOnClickListener {
            viewModel.resetear()
            parentFragmentManager.popBackStack(null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        // Botón Descargar comprobante
        binding.btnDescargarComprobante.setOnClickListener {
            iniciarDescargaComprobante()
        }
    }

    private fun iniciarDescargaComprobante() {
        val numeroPR = arguments?.getString("numeroPR") ?: ""
        val preRegistro = preRegistroViewModel.preRegistroGuardado.value

        val dialog = ComprobanteGenerandoDialog()
        dialog.isCancelable = false
        dialog.show(parentFragmentManager, "ComprobanteGenerandoDialog")

        comprobanteViewModel.generarComprobante(
            context      = requireContext(),
            numeroPR     = numeroPR,
            remitente    = preRegistro?.remitente ?: "",
            destinatario = preRegistro?.destinatario ?: "",
            origen       = "Trujillo",
            destino      = cotizacionViewModel.destinoSeleccionado.value ?: "",
            costo        = cotizacionViewModel.costoEstimado.value ?: 0.0,
            peso         = cotizacionViewModel.peso.value ?: 0.0,
            metodoPago   = preRegistro?.metodoPago ?: "digital"
        )

        comprobanteViewModel.estado.observe(viewLifecycleOwner) { estado ->
            when (estado) {
                is ComprobanteViewModel.ComprobanteEstado.Descargado -> {
                    dialog.dismiss()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.contenedorFragment,
                            ComprobanteDescargadoFragment.newInstance(estado.numeroPR))
                        .addToBackStack(null)
                        .commit()
                }
                is ComprobanteViewModel.ComprobanteEstado.Error -> {
                    dialog.dismiss()
                    android.widget.Toast.makeText(
                        requireContext(), estado.mensaje, android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}