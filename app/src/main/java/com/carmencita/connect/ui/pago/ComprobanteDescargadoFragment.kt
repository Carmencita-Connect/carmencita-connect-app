package com.carmencita.connect.ui.pago

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.databinding.FragmentComprobanteDescargadoBinding
import com.carmencita.connect.viewmodel.ComprobanteViewModel
import com.carmencita.connect.viewmodel.CotizacionViewModel
import com.carmencita.connect.viewmodel.PreRegistroViewModel

class ComprobanteDescargadoFragment : Fragment() {

    private var _binding: FragmentComprobanteDescargadoBinding? = null
    private val binding get() = _binding!!

    private val comprobanteViewModel: ComprobanteViewModel by activityViewModels()
    private val cotizacionViewModel: CotizacionViewModel by activityViewModels()
    private val preRegistroViewModel: PreRegistroViewModel by activityViewModels()

    companion object {
        fun newInstance(numeroPR: String) = ComprobanteDescargadoFragment().apply {
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
        _binding = FragmentComprobanteDescargadoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString("numeroPR")?.let { numero ->
            binding.tvNumeroBoleta.text = numero
        }

        binding.btnInicioComprobante.setOnClickListener {
            comprobanteViewModel.resetear()
            cotizacionViewModel.resetear()
            preRegistroViewModel.resetear()
            parentFragmentManager.popBackStack(
                null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}