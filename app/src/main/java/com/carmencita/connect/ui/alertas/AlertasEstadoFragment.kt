package com.carmencita.connect.ui.alertas

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.carmencita.connect.databinding.FragmentAlertasEstadoBinding
import com.carmencita.connect.model.AlertaCambioEstado
import com.carmencita.connect.viewmodel.AlertasEstadoViewModel

class AlertasEstadoFragment : Fragment() {

    private var _binding: FragmentAlertasEstadoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AlertasEstadoViewModel by viewModels()
    private val notifier by lazy { AlertaLocalNotifier(requireContext()) }
    private var actualizandoSwitch = false

    private val solicitarPermisoNotificaciones = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permitido ->
        if (permitido) {
            viewModel.configurarAlertas(true)
        } else {
            binding.switchAlertasEstado.isChecked = false
            viewModel.configurarAlertas(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlertasEstadoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchAlertasEstado.setOnCheckedChangeListener { _, activo ->
            if (actualizandoSwitch) return@setOnCheckedChangeListener

            if (activo && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                solicitarPermisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                viewModel.configurarAlertas(activo)
            }
        }

        binding.btnSimularCambioEstado.setOnClickListener {
            viewModel.simularCambioEstado()
        }

        viewModel.alertasActivas.observe(viewLifecycleOwner) { activo ->
            if (binding.switchAlertasEstado.isChecked != activo) {
                actualizandoSwitch = true
                binding.switchAlertasEstado.isChecked = activo
                actualizandoSwitch = false
            }
            binding.tvEstadoConfiguracion.text = if (activo) {
                "Alertas activas"
            } else {
                "Alertas desactivadas"
            }
        }

        viewModel.estadoActual.observe(viewLifecycleOwner) { estado ->
            binding.tvEstadoActualAlerta.text = estado
        }

        viewModel.ultimoCambio.observe(viewLifecycleOwner) { cambio ->
            cambio ?: return@observe
            mostrarUltimoCambio(cambio)
            if (binding.switchAlertasEstado.isChecked) {
                notifier.mostrar(cambio)
            }
        }

        viewModel.mensaje.observe(viewLifecycleOwner) { mensaje ->
            binding.tvMensajeAlerta.visibility = if (mensaje.isBlank()) View.GONE else View.VISIBLE
            binding.tvMensajeAlerta.text = mensaje
        }

        viewModel.cargarConfiguracion()
    }

    private fun mostrarUltimoCambio(cambio: AlertaCambioEstado) {
        binding.tvUltimaAlerta.visibility = View.VISIBLE
        binding.tvUltimaAlerta.text = cambio.mensaje
        binding.tvIndicacionRecojo.visibility =
            if (cambio.requiereRecojo) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
