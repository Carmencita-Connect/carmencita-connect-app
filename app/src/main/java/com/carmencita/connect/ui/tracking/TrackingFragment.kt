package com.carmencita.connect.ui.tracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.R
import com.carmencita.connect.databinding.FragmentTrackingBinding
import com.carmencita.connect.databinding.ItemHistorialTrackingBinding
import com.carmencita.connect.viewmodel.SesionViewModel
import com.carmencita.connect.viewmodel.TrackingViewModel

class TrackingFragment : Fragment() {

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TrackingViewModel by activityViewModels()
    private val sesionViewModel: SesionViewModel by activityViewModels()
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
        sesionViewModel.cargarSesion()

        binding.btnBuscar.setOnClickListener {
            ocultarTeclado()
            viewModel.buscarEncomienda(
                binding.etNumeroGuia.text.toString().trim()
            )
        }

        binding.tvLimpiarHistorial.setOnClickListener {
            viewModel.limpiarHistorial()
        }

        viewModel.historial.observe(viewLifecycleOwner, ::mostrarHistorial)
        sesionViewModel.sesionActiva.observe(viewLifecycleOwner) { activa ->
            binding.panelHistorial.visibility = if (activa) View.VISIBLE else View.GONE
            viewModel.configurarHistorial(activa)
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
                        viewModel.resetear()
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
                        viewModel.resetear()
                    }
                }
            }
        }
    }

    private fun mostrarHistorial(codigos: List<String>) {
        val historialVacio = codigos.isEmpty()
        binding.contenedorHistorial.removeAllViews()
        binding.contenedorHistorial.visibility = if (historialVacio) View.GONE else View.VISIBLE
        binding.estadoHistorialVacio.visibility = if (historialVacio) View.VISIBLE else View.GONE
        binding.tvLimpiarHistorial.visibility = if (historialVacio) View.GONE else View.VISIBLE

        codigos.forEach { codigo ->
            val itemBinding = ItemHistorialTrackingBinding.inflate(
                layoutInflater,
                binding.contenedorHistorial,
                false
            )
            itemBinding.tvCodigoHistorial.text = codigo
            itemBinding.root.setOnClickListener {
                binding.etNumeroGuia.setText(codigo)
                binding.etNumeroGuia.setSelection(codigo.length)
                ocultarTeclado()
                viewModel.buscarEncomienda(codigo)
            }
            binding.contenedorHistorial.addView(itemBinding.root)
        }
    }

    private fun ocultarTeclado() {
        val inputMethodManager = requireContext().getSystemService(
            android.content.Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.etNumeroGuia.windowToken, 0)
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
