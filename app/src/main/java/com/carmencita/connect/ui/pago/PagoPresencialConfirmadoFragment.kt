package com.carmencita.connect.ui.pago

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.databinding.FragmentPagoPresencialConfirmadoBinding
import com.carmencita.connect.viewmodel.CotizacionViewModel
import com.carmencita.connect.viewmodel.PagoPresencialViewModel
import com.carmencita.connect.viewmodel.PagoViewModel
import com.carmencita.connect.viewmodel.PreRegistroViewModel

class PagoPresencialConfirmadoFragment : Fragment() {

    private var _binding: FragmentPagoPresencialConfirmadoBinding? = null
    private val binding get() = _binding!!

    private val pagoViewModel: PagoViewModel by activityViewModels()
    private val pagoPresencialViewModel: PagoPresencialViewModel by activityViewModels()
    private val cotizacionViewModel: CotizacionViewModel by activityViewModels()
    private val preRegistroViewModel: PreRegistroViewModel by activityViewModels()

    companion object {
        fun newInstance(numeroPR: String) = PagoPresencialConfirmadoFragment().apply {
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
        _binding = FragmentPagoPresencialConfirmadoBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString("numeroPR")?.let { numero ->
            binding.tvNumeroPresencial.text = numero
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : androidx.activity.OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // No permite volver atrás
                }
            }
        )

        binding.btnInicioPresencial.setOnClickListener {
            pagoViewModel.resetear()
            pagoPresencialViewModel.resetear()
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