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
import com.stripe.android.Stripe

class PagoDigitalFragment : Fragment() {

    private var _binding: FragmentPagoDigitalBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PagoViewModel by activityViewModels()
    private val cotizacionViewModel: CotizacionViewModel by activityViewModels()
    private val preRegistroViewModel: PreRegistroViewModel by activityViewModels()

    private var validandoDialog: PagoValidandoDialog? = null

    private val stripe by lazy {
        Stripe(requireContext(), "pk_test_51TZNAe40wQ1eY6er9ekzm9Z68jDydYtPScUWgrYwkZYyqgXz0CwVtdl7djFfmkkMKFkO6tp30X0knkfvqTa4pKzE00y6wH6Hpu")
    }

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

        cargarDetallesEnvio()

        binding.btnTarjeta.setOnClickListener {
            seleccionarMetodo("Tarjeta de crédito / débito")
        }

        binding.btnYape.setOnClickListener {
            seleccionarMetodo("Yape")
        }

        binding.btnConfirmar.setOnClickListener {
            val metodo = viewModel.metodoPago.value ?: ""
            if (metodo.isEmpty()) {
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = "Selecciona un método de pago"
                return@setOnClickListener
            }
            if (metodo == "Tarjeta de crédito / débito") {
                procesarTarjeta()
            } else {
                mostrarDialog()
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                    val monto = preRegistroViewModel.preRegistroGuardado.value
                        ?.encomienda?.tarifa?.costo ?: 0.0
                    viewModel.onPagoExitoso("yape_ok", monto)
                }, 2000)
            }
        }

        binding.btnEliminarPago.setOnClickListener {
            viewModel.cancelarPago()
            preRegistroViewModel.limpiarPreRegistroGuardado()
            parentFragmentManager.popBackStack()
        }

        viewModel.metodoPago.observe(viewLifecycleOwner) { metodo ->
            when (metodo) {
                "Tarjeta de crédito / débito" -> {
                    binding.radioTarjeta.isChecked = true
                    binding.radioYape.isChecked = false
                    binding.btnTarjeta.setBackgroundResource(R.drawable.bg_metodo_pago_seleccionado)
                    binding.btnYape.setBackgroundResource(R.drawable.bg_boton_blanco)
                    binding.cardInputWidget.visibility = View.VISIBLE
                    binding.tvModoPago.text = metodo
                }
                "Yape" -> {
                    binding.radioTarjeta.isChecked = false
                    binding.radioYape.isChecked = true
                    binding.btnTarjeta.setBackgroundResource(R.drawable.bg_boton_blanco)
                    binding.btnYape.setBackgroundResource(R.drawable.bg_metodo_pago_seleccionado)
                    binding.cardInputWidget.visibility = View.GONE
                    binding.tvModoPago.text = metodo
                }
                else -> {
                    binding.radioTarjeta.isChecked = false
                    binding.radioYape.isChecked = false
                    binding.btnTarjeta.setBackgroundResource(R.drawable.bg_boton_blanco)
                    binding.btnYape.setBackgroundResource(R.drawable.bg_boton_blanco)
                    binding.cardInputWidget.visibility = View.GONE
                }
            }
        }

        viewModel.estado.observe(viewLifecycleOwner) { estado ->
            when (estado) {
                is PagoViewModel.PagoEstado.Idle -> cerrarDialog()
                is PagoViewModel.PagoEstado.Validando -> mostrarDialog()
                is PagoViewModel.PagoEstado.Confirmado -> {
                    cerrarDialog()
                    preRegistroViewModel.marcarComoPagado()
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
                is PagoViewModel.PagoEstado.Rechazado -> {
                    cerrarDialog()
                    binding.cardInputWidget.clear()
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = estado.mensaje
                }
                is PagoViewModel.PagoEstado.Cancelado -> cerrarDialog()
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
                    preRegistroViewModel.limpiarPreRegistroGuardado()
                    parentFragmentManager.popBackStack()
                }
            }
        )
    }

    private fun procesarTarjeta() {
        val params = binding.cardInputWidget.paymentMethodCreateParams

        if (params == null) {
            binding.tvError.visibility = View.VISIBLE
            binding.tvError.text = "Ingresa los datos de tu tarjeta"
            return
        }

        val monto = preRegistroViewModel.preRegistroGuardado.value
            ?.encomienda?.tarifa?.costo ?: 0.0

        mostrarDialog()

        stripe.createPaymentMethod(
            paymentMethodCreateParams = params,
            callback = object : com.stripe.android.ApiResultCallback<com.stripe.android.model.PaymentMethod> {
                override fun onSuccess(result: com.stripe.android.model.PaymentMethod) {
                    viewModel.onPagoExitoso(result.id ?: "stripe_ok", monto)
                }
                override fun onError(e: Exception) {
                    viewModel.onPagoRechazado(e.message ?: "Tarjeta rechazada por la entidad bancaria")
                }
            }
        )
    }

    private fun seleccionarMetodo(metodo: String) {
        viewModel.seleccionarMetodo(metodo)
        binding.tvModoPago.text = metodo
        binding.tvError.visibility = View.GONE
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
        binding.tvCostoDetalle.text = "%.2f".format(
            preRegistro?.encomienda?.tarifa?.costo ?: 0.0
        )
        val largo = preRegistro?.encomienda?.largo ?: 0.0
        val ancho = preRegistro?.encomienda?.ancho ?: 0.0
        val alto  = preRegistro?.encomienda?.alto ?: 0.0
        binding.tvMedidasDetalle.text = "%.0f × %.0f × %.0f cm".format(ancho, alto, largo)
        binding.tvPesoDetalle.text = "%.2f".format(preRegistro?.encomienda?.peso ?: 0.0)
        binding.tvModoPago.text = preRegistroViewModel.metodoPago.value ?: ""
    }

    private fun mostrarDialog() {
        if (validandoDialog == null) validandoDialog = PagoValidandoDialog()
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