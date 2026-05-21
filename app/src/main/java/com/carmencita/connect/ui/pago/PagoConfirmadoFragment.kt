package com.carmencita.connect.ui.pago

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.R
import com.carmencita.connect.databinding.FragmentPagoConfirmadoBinding
import com.carmencita.connect.viewmodel.PagoViewModel

class PagoConfirmadoFragment : Fragment() {

    private var _binding: FragmentPagoConfirmadoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PagoViewModel by activityViewModels()

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
            android.widget.Toast.makeText(
                requireContext(),
                "Comprobante descargado",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}