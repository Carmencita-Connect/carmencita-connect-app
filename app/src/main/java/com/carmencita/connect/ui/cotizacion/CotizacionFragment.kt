package com.carmencita.connect.ui.cotizacion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.carmencita.connect.databinding.FragmentCotizacionBinding
import com.carmencita.connect.viewmodel.CotizacionViewModel
import com.carmencita.connect.R

class CotizacionFragment : Fragment() {

    private var _binding: FragmentCotizacionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CotizacionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCotizacionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botón calcular
        binding.btnCalcular.setOnClickListener {

            viewModel.calcularTarifa(
                largo  = binding.etLargo.text.toString().trim(),
                ancho  = binding.etAncho.text.toString().trim(),
                alto   = binding.etAlto.text.toString().trim(),
                peso   = binding.etPeso.text.toString().trim(),
                origen = binding.spinnerOrigen.selectedItem.toString(),
                destino = binding.spinnerDestino.selectedItem.toString()
            )
        }

        binding.etPeso.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                val imm = requireContext().getSystemService(
                    android.content.Context.INPUT_METHOD_SERVICE
                ) as android.view.inputmethod.InputMethodManager
                imm.hideSoftInputFromWindow(binding.etPeso.windowToken, 0)
                binding.etPeso.clearFocus()
            }
            false
        }

        // Botón realizar pre-registro
        binding.btnPreRegistro.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment,
                    com.carmencita.connect.ui.preregistro.PreRegistroFragment())
                .addToBackStack(null)
                .commit()
        }

        viewModel.costoEstimado.observe(viewLifecycleOwner) { costo ->
            if (costo == 0.0) {
                binding.layoutResultado.visibility = View.GONE
                binding.btnPreRegistro.visibility  = View.GONE
                binding.etLargo.text.clear()
                binding.etAncho.text.clear()
                binding.etAlto.text.clear()
                binding.etPeso.text.clear()
            } else {
                binding.layoutResultado.visibility = View.VISIBLE
                binding.btnPreRegistro.visibility  = View.VISIBLE
                binding.tvTarifa.text = "%.2f".format(costo)
                binding.tvError.visibility = View.GONE
            }
        }
        // Observar errores
        viewModel.error.observe(viewLifecycleOwner) { mensaje ->
            if (mensaje.isNotEmpty()) {
                binding.tvError.visibility     = View.VISIBLE
                binding.tvError.text           = mensaje
                binding.layoutResultado.visibility = View.GONE
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