package com.carmencita.connect.ui.pago

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.R
import com.carmencita.connect.databinding.FragmentPagoPresencialBinding
import com.carmencita.connect.viewmodel.CotizacionViewModel
import com.carmencita.connect.viewmodel.PreRegistroViewModel

class PagoPresencialFragment : Fragment() {

    private var _binding: FragmentPagoPresencialBinding? = null
    private val binding get() = _binding!!

    private val cotizacionViewModel: CotizacionViewModel by activityViewModels()
    private val preRegistroViewModel: PreRegistroViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagoPresencialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cargarDetallesEnvio()

        // Botón confirmar
        binding.btnConfirmarPresencial.setOnClickListener {
            val numero = "PR-2026-%07d".format((1..9999999).random())
            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.contenedorFragment,
                    PagoConfirmadoFragment.newInstance(numero),
                    "confirmado"
                )
                .addToBackStack(null)
                .commit()
        }

        // Botón eliminar
        binding.btnEliminarPresencial.setOnClickListener {
            cotizacionViewModel.resetear()
            preRegistroViewModel.resetear()
            parentFragmentManager.popBackStack(
                null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }

        // Botón actualizar — vuelve al pre-registro
        binding.btnActualizarPresencial.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun cargarDetallesEnvio() {
        val preRegistro = preRegistroViewModel.preRegistroGuardado.value
        binding.tvOrigenPresencial.text = "Trujillo"
        binding.tvDestinoPresencial.text =
            cotizacionViewModel.destinoSeleccionado.value ?: ""
        binding.tvRemitentePresencial.text = preRegistro?.remitente ?: ""
        binding.tvDestinatarioPresencial.text = preRegistro?.destinatario ?: ""
        binding.tvCostoPresencial.text = "%.2f".format(
            cotizacionViewModel.costoEstimado.value ?: 0.0
        )
        val largo = cotizacionViewModel.largo.value ?: 0.0
        val ancho = cotizacionViewModel.ancho.value ?: 0.0
        val alto = cotizacionViewModel.alto.value ?: 0.0
        binding.tvMedidasPresencial.text =
            "%.0f × %.0f × %.0f cm".format(ancho, alto, largo)
        binding.tvPesoPresencial.text = "%.2f".format(
            cotizacionViewModel.peso.value ?: 0.0
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}