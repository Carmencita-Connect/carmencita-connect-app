package com.carmencita.connect.ui.notificaciones

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.carmencita.connect.R
import com.carmencita.connect.databinding.FragmentHistorialNotificacionesBinding
import com.carmencita.connect.databinding.ItemNotificacionBinding
import com.carmencita.connect.model.Notificacion
import com.carmencita.connect.viewmodel.HistorialNotificacionesViewModel

class HistorialNotificacionesFragment : Fragment() {

    private var _binding: FragmentHistorialNotificacionesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistorialNotificacionesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistorialNotificacionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLimpiarHistorialNotificaciones.setOnClickListener {
            confirmarLimpieza()
        }

        viewModel.notificaciones.observe(viewLifecycleOwner, ::mostrarNotificaciones)
        viewModel.cargarHistorial()
    }

    private fun mostrarNotificaciones(notificaciones: List<Notificacion>) {
        val historialVacio = notificaciones.isEmpty()
        binding.contenedorNotificaciones.removeAllViews()
        binding.tvHistorialNotificacionesVacio.visibility =
            if (historialVacio) View.VISIBLE else View.GONE
        binding.btnLimpiarHistorialNotificaciones.visibility =
            if (historialVacio) View.GONE else View.VISIBLE

        notificaciones.forEach { notificacion ->
            val item = ItemNotificacionBinding.inflate(
                layoutInflater,
                binding.contenedorNotificaciones,
                false
            )
            item.tvTituloNotificacion.text = notificacion.titulo
            item.tvMensajeNotificacion.text = notificacion.mensaje
            item.tvFechaNotificacion.text = notificacion.fecha
            item.tvEstadoNotificacion.text = notificacion.estado
            binding.contenedorNotificaciones.addView(item.root)
        }
    }

    private fun confirmarLimpieza() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.hu10_confirmar_limpiar_titulo)
            .setMessage(R.string.hu10_confirmar_limpiar_mensaje)
            .setNegativeButton(R.string.hu10_cancelar, null)
            .setPositiveButton(R.string.hu10_limpiar) { _, _ ->
                viewModel.limpiarHistorial()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
