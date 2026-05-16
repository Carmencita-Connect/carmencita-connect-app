package com.carmencita.connect.ui.preregistro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.databinding.FragmentPreregistroBinding
import com.carmencita.connect.viewmodel.CotizacionViewModel
import com.carmencita.connect.viewmodel.PreRegistroViewModel

class PreRegistroFragment : Fragment() {

    private var _binding: FragmentPreregistroBinding? = null
    private val binding get() = _binding!!

    // ViewModel propio
    private val viewModel: PreRegistroViewModel by activityViewModels()

    // ViewModel compartido con Cotización — para traer los datos
    private val cotizacionViewModel: CotizacionViewModel by activityViewModels()

    // Método de pago seleccionado
    private var metodoPagoSeleccionado: String = ""

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
        viewModel.resetear()

        // Botón pago en agencia
        binding.btnPagoAgencia.setOnClickListener {
            metodoPagoSeleccionado = "agencia"
            binding.btnPagoAgencia.setBackgroundResource(
                com.carmencita.connect.R.drawable.bg_boton_seleccionado
            )
            binding.btnPagoDigital.setBackgroundResource(
                com.carmencita.connect.R.drawable.bg_boton_blanco
            )
        }

        // Botón pago digital
        binding.btnPagoDigital.setOnClickListener {
            metodoPagoSeleccionado = "digital"
            binding.btnPagoDigital.setBackgroundResource(
                com.carmencita.connect.R.drawable.bg_boton_seleccionado
            )
            binding.btnPagoAgencia.setBackgroundResource(
                com.carmencita.connect.R.drawable.bg_boton_blanco
            )
        }

        // Botón continuar
        binding.btnContinuar.setOnClickListener {
            val imm = requireContext().getSystemService(
                android.content.Context.INPUT_METHOD_SERVICE
            ) as android.view.inputmethod.InputMethodManager
            imm.hideSoftInputFromWindow(binding.etRemitente.windowToken, 0)

            // Traer datos de cotización
            val costo   = cotizacionViewModel.costoEstimado.value ?: 0.0
            val destino = cotizacionViewModel.destinoSeleccionado.value ?: ""
            val largo   = cotizacionViewModel.largo.value ?: 0.0
            val ancho   = cotizacionViewModel.ancho.value ?: 0.0
            val alto    = cotizacionViewModel.alto.value ?: 0.0
            val peso    = cotizacionViewModel.peso.value ?: 0.0

            viewModel.guardarPreRegistro(
                remitente      = binding.etRemitente.text.toString().trim(),
                destinatario   = binding.etDestinatario.text.toString().trim(),
                metodoPago     = metodoPagoSeleccionado,
                destino        = destino,
                largo          = largo,
                ancho          = ancho,
                alto           = alto,
                peso           = peso,
                costoEstimado  = costo
            )
        }

        // Botón eliminar
        binding.btnEliminar.setOnClickListener {
            cotizacionViewModel.resetear()
            parentFragmentManager.popBackStack()
        }

        // Observar resultado
        viewModel.preRegistroGuardado.observe(viewLifecycleOwner) { preRegistro ->
            preRegistro ?: return@observe  // ← ignora si es null
            android.widget.Toast.makeText(
                requireContext(),
                "Pre-registro #${preRegistro.id} guardado",
                android.widget.Toast.LENGTH_SHORT
            ).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}