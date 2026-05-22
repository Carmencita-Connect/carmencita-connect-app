package com.carmencita.connect.ui.pago

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.carmencita.connect.databinding.FragmentPagoPresencialConfirmadoBinding

class PagoPresencialConfirmadoFragment : Fragment() {

    private var _binding: FragmentPagoPresencialConfirmadoBinding? = null
    private val binding get() = _binding!!

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

        binding.btnInicioPresencial.setOnClickListener {
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