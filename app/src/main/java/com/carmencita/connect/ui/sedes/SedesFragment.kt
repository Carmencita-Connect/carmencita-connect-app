package com.carmencita.connect.ui.sedes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.carmencita.connect.R
import com.carmencita.connect.data.FormateadorTelefonoSede
import com.carmencita.connect.databinding.FragmentSedesBinding
import com.carmencita.connect.databinding.ItemSedeBinding
import com.carmencita.connect.model.Sede
import com.carmencita.connect.viewmodel.SedesViewModel

class SedesFragment : Fragment() {

    private var _binding: FragmentSedesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SedesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSedesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.sedes.observe(viewLifecycleOwner) { sedes ->
            mostrarSedes(sedes)
        }
    }

    private fun mostrarSedes(sedes: List<Sede>) {
        binding.contenedorSedes.removeAllViews()

        sedes.forEach { sede ->
            val item = ItemSedeBinding.inflate(
                layoutInflater,
                binding.contenedorSedes,
                false
            )

            item.tvNombreSede.text = sede.nombre.uppercase()
            item.tvDireccionSede.text = sede.direccion
            item.tvTelefonoSede.text = sede.telefono
            item.tvHorarioSede.text = sede.horario

            item.root.setOnClickListener { abrirUbicacion(sede) }
            item.btnLlamarSede.setOnClickListener { llamarSede(sede.telefono) }

            binding.contenedorSedes.addView(item.root)
        }
    }

    private fun abrirUbicacion(sede: Sede) {
        val etiqueta = Uri.encode(sede.nombre)
        val ubicacion = Uri.parse(
            "geo:${sede.latitud},${sede.longitud}?q=${sede.latitud},${sede.longitud}($etiqueta)"
        )
        val intentMapa = Intent(Intent.ACTION_VIEW, ubicacion)

        if (intentMapa.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intentMapa)
        } else {
            val web = Uri.parse(
                "https://www.google.com/maps/search/?api=1&query=${sede.latitud},${sede.longitud}"
            )
            startActivity(Intent(Intent.ACTION_VIEW, web))
        }
    }

    private fun llamarSede(telefono: String) {
        val numero = FormateadorTelefonoSede.normalizarParaMarcador(telefono)
        if (numero == null) {
            Toast.makeText(
                requireContext(),
                R.string.hu14_telefono_no_disponible,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val intentLlamada = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$numero"))
        if (intentLlamada.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intentLlamada)
        } else {
            Toast.makeText(
                requireContext(),
                R.string.hu14_marcador_no_disponible,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
