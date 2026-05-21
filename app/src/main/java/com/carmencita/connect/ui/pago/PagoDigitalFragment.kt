package com.carmencita.connect.ui.pago

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.R
import com.carmencita.connect.databinding.FragmentPagoDigitalBinding
import com.carmencita.connect.viewmodel.CotizacionViewModel
import com.carmencita.connect.viewmodel.PagoViewModel
import com.carmencita.connect.viewmodel.PreRegistroViewModel

class PagoDigitalFragment : Fragment() {

    private var _binding: FragmentPagoDigitalBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PagoViewModel by activityViewModels()
    private val cotizacionViewModel: CotizacionViewModel by activityViewModels()
    private val preRegistroViewModel: PreRegistroViewModel by activityViewModels()

    private var validandoDialog: PagoValidandoDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagoDigitalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.resetear()
        cargarDetallesEnvio()

        // Selección de método de pago
        binding.btnTarjeta.setOnClickListener {
            seleccionarMetodo("Tarjeta de crédito / débito")
            binding.radioTarjeta.isChecked = true
            binding.radioYape.isChecked = false
            binding.btnTarjeta.setBackgroundResource(R.drawable.bg_metodo_pago_seleccionado)
            binding.btnYape.setBackgroundResource(R.drawable.bg_boton_blanco)
        }

        binding.btnYape.setOnClickListener {
            seleccionarMetodo("Yape")
            binding.radioTarjeta.isChecked = false
            binding.radioYape.isChecked = true
            binding.btnTarjeta.setBackgroundResource(R.drawable.bg_boton_blanco)
            binding.btnYape.setBackgroundResource(R.drawable.bg_metodo_pago_seleccionado)
        }

        // Botón confirmar
        binding.btnConfirmar.setOnClickListener {
            viewModel.confirmarPago()
        }

        // Botón eliminar
        binding.btnEliminarPago.setOnClickListener {
            viewModel.cancelarPago()
            cotizacionViewModel.resetear()
            preRegistroViewModel.resetear()
            parentFragmentManager.popBackStack(null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        // Botón actualizar — vuelve al pre-registro
        binding.btnActualizar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Observar estado
        viewModel.estado.observe(viewLifecycleOwner) { estado ->
            when (estado) {
                is PagoViewModel.PagoEstado.Idle -> {
                    cerrarDialog()
                }
                is PagoViewModel.PagoEstado.Validando -> {
                    mostrarDialog()
                }
                is PagoViewModel.PagoEstado.Confirmado -> {
                    cerrarDialog()
                    if (parentFragmentManager.findFragmentByTag("confirmado") == null) {
                        parentFragmentManager.beginTransaction()
                            .replace(
                                R.id.contenedorFragment,
                                PagoConfirmadoFragment.newInstance(estado.numeroPR),
                                "confirmado"
                            )
                            .addToBackStack(null)
                            .commit()
                    }
                }
                is PagoViewModel.PagoEstado.Cancelado -> {
                    cerrarDialog()
                }
            }
        }

        // Observar errores
        viewModel.error.observe(viewLifecycleOwner) { mensaje ->
            if (mensaje.isNotEmpty()) {
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = mensaje
            } else {
                binding.tvError.visibility = View.GONE
            }
        }
    }

    private fun seleccionarMetodo(metodo: String) {
        viewModel.seleccionarMetodo(metodo)
        binding.tvModoPago.text = metodo
    }

    private fun cargarDetallesEnvio() {
        val preRegistro = preRegistroViewModel.preRegistroGuardado.value

        binding.tvOrigenDetalle.text = "Trujillo"
        binding.tvDestinoDetalle.text = cotizacionViewModel.destinoSeleccionado.value ?: ""
        binding.tvRemitenteDetalle.text = preRegistro?.remitente ?: ""
        binding.tvDestinatarioDetalle.text = preRegistro?.destinatario ?: ""
        binding.tvCostoDetalle.text = "%.2f".format(
            cotizacionViewModel.costoEstimado.value ?: 0.0
        )

        val largo = cotizacionViewModel.largo.value ?: 0.0
        val ancho = cotizacionViewModel.ancho.value ?: 0.0
        val alto = cotizacionViewModel.alto.value ?: 0.0
        binding.tvMedidasDetalle.text = "%.0f × %.0f × %.0f cm".format(ancho, alto, largo)
        binding.tvPesoDetalle.text = "%.2f".format(
            cotizacionViewModel.peso.value ?: 0.0
        )
    }

    private fun mostrarDialog() {
        if (validandoDialog == null) {
            validandoDialog = PagoValidandoDialog()
        }
        if (!validandoDialog!!.isAdded) {
            validandoDialog!!.show(parentFragmentManager, "validandoPago")
        }
    }

    private fun cerrarDialog() {
        validandoDialog?.dismiss()
        validandoDialog = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}