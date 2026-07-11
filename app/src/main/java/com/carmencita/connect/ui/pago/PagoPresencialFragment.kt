package com.carmencita.connect.ui.pago

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.R
import com.carmencita.connect.databinding.FragmentPagoPresencialBinding
import com.carmencita.connect.viewmodel.PagoPresencialViewModel
import com.carmencita.connect.viewmodel.PreRegistroViewModel

class PagoPresencialFragment : Fragment() {

    private var _binding: FragmentPagoPresencialBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PagoPresencialViewModel by activityViewModels()
    private val preRegistroViewModel: PreRegistroViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagoPresencialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cargarDetallesEnvio()

        binding.btnConfirmar.setOnClickListener {
            val monto = preRegistroViewModel.preRegistroGuardado.value
                ?.encomienda?.tarifa?.costo ?: 0.0
            preRegistroViewModel.marcarComoPagado()
            viewModel.confirmarPago(monto)
        }

        viewModel.pagoConfirmado.observe(viewLifecycleOwner) { pago ->
            pago ?: return@observe
            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.contenedorFragment,
                    PagoPresencialConfirmadoFragment.newInstance(pago.numeroPR),
                    "confirmado"
                )
                .addToBackStack(null)
                .commit()
            viewModel.resetear()
        }

        binding.btnEliminarPago.setOnClickListener {
            viewModel.resetear()
            preRegistroViewModel.limpiarPreRegistroGuardado()
            parentFragmentManager.popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : androidx.activity.OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.resetear()
                    preRegistroViewModel.limpiarPreRegistroGuardado()
                    parentFragmentManager.popBackStack()
                }
            }
        )
    }

    private fun cargarDetallesEnvio() {
        val preRegistro = preRegistroViewModel.preRegistroGuardado.value
        binding.tvOrigenDetalle.text = preRegistro?.encomienda?.origen ?: "Trujillo"
        binding.tvDestinoDetalle.text = preRegistro?.encomienda?.destino ?: ""
        binding.tvRemitenteDetalle.text = preRegistro?.remitente?.nombre ?: ""
        binding.tvDniRemitenteDetalle.text = preRegistro?.remitente?.dni ?: ""
        binding.tvTelefonoRemitenteDetalle.text = preRegistro?.remitente?.telefono ?: ""
        binding.tvDireccionRemitenteDetalle.text = preRegistro?.remitente?.direccion ?: ""
        binding.tvDestinatarioDetalle.text = preRegistro?.destinatario?.nombre ?: ""
        binding.tvDniDestinatarioDetalle.text = preRegistro?.destinatario?.dni ?: ""
        binding.tvTelefonoDestinatarioDetalle.text = preRegistro?.destinatario?.telefono ?: ""
        binding.tvDireccionDestinatarioDetalle.text = preRegistro?.destinatario?.direccion ?: ""
        binding.tvDescripcionCargaDetalle.text = preRegistro?.descripcionCarga ?: ""
        binding.tvModoPago.text = "Pago en agencia"
        binding.tvCostoDetalle.text = "%.2f".format(
            preRegistro?.encomienda?.tarifa?.costo ?: 0.0
        )
        val largo = preRegistro?.encomienda?.largo ?: 0.0
        val ancho = preRegistro?.encomienda?.ancho ?: 0.0
        val alto  = preRegistro?.encomienda?.alto ?: 0.0
        binding.tvMedidasDetalle.text = "%.0f × %.0f × %.0f cm".format(ancho, alto, largo)
        binding.tvPesoDetalle.text = "%.2f".format(preRegistro?.encomienda?.peso ?: 0.0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
