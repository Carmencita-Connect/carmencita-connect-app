package com.carmencita.connect.ui.tracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.R
import com.carmencita.connect.databinding.FragmentTrackingBinding
import com.carmencita.connect.viewmodel.TrackingViewModel

class TrackingFragment : Fragment() {

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TrackingViewModel by activityViewModels()
    private var validandoDialog: TrackingValidandoDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBuscar.setOnClickListener {
            val imm = requireContext().getSystemService(
                android.content.Context.INPUT_METHOD_SERVICE
            ) as android.view.inputmethod.InputMethodManager
            imm.hideSoftInputFromWindow(binding.etNumeroGuia.windowToken, 0)

            viewModel.buscarEncomienda(
                binding.etNumeroGuia.text.toString().trim()
            )
        }

        viewModel.estado.observe(viewLifecycleOwner) { estado ->
            when (estado) {
                is TrackingViewModel.TrackingEstado.Idle -> {
                    cerrarDialog()
                }
                is TrackingViewModel.TrackingEstado.Validando -> {
                    mostrarDialog()
                }
                is TrackingViewModel.TrackingEstado.CodigoInvalido -> {
                    cerrarDialog()
                    if (parentFragmentManager.findFragmentByTag("invalido") == null) {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.contenedorFragment,
                                TrackingInvalidoFragment.newInstance(), "invalido")
                            .addToBackStack(null)
                            .commit()
                    }
                }
                is TrackingViewModel.TrackingEstado.Resultado -> {
                    cerrarDialog()
                    if (parentFragmentManager.findFragmentByTag("resultado") == null) {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.contenedorFragment,
                                TrackingResultadoFragment.newInstance(estado.encomienda),
                                "resultado")
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }
        }
    }

    private fun mostrarDialog() {
        if (validandoDialog == null) {
            validandoDialog = TrackingValidandoDialog()
        }
        if (!validandoDialog!!.isAdded) {
            validandoDialog!!.show(parentFragmentManager, "validando")
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