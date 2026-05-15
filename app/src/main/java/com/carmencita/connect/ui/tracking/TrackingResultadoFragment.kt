package com.carmencita.connect.ui.tracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.databinding.LayoutTrackingResultadoBinding
import com.carmencita.connect.model.Encomienda
import com.carmencita.connect.viewmodel.TrackingViewModel

class TrackingResultadoFragment : Fragment() {

    private var _binding: LayoutTrackingResultadoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TrackingViewModel by activityViewModels()

    companion object {
        fun newInstance(e: Encomienda) = TrackingResultadoFragment().apply {
            arguments = Bundle().apply {
                putString("guia",         e.numeroGuia)
                putString("estado",       e.estado)
                putString("remitente",    e.remitente)
                putString("destinatario", e.destinatario)
                putString("destino",      e.destino)
                putString("peso",         "${e.peso}kg")
                putString("f_reg",        e.fechaRegistro ?: "")
                putString("f_trans",      e.fechaTransito ?: "")
                putString("f_agencia",    e.fechaAgencia  ?: "")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutTrackingResultadoBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->
            binding.tvNumeroGuia.text      = args.getString("guia")
            binding.tvEstado.text          = args.getString("estado")
            binding.tvRemitente.text       = args.getString("remitente")
            binding.tvDestinatario.text    = args.getString("destinatario")
            binding.tvDestino.text         = args.getString("destino")
            binding.tvPeso.text            = args.getString("peso")
            binding.tvFechaRegistrado.text = args.getString("f_reg")
            binding.tvFechaTransito.text   = args.getString("f_trans")
            binding.tvFechaAgencia.text    = args.getString("f_agencia")
        }

        binding.btnVolver.setOnClickListener {
            viewModel.resetear()
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}