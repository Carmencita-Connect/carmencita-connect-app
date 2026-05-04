package com.carmencita.connect.ui.preregistro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.carmencita.connect.R
import com.carmencita.connect.viewmodel.PreRegistroViewModel

class PreRegistroFragment : Fragment() {

    private val viewModel: PreRegistroViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preregistro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etRemitente = view.findViewById<EditText>(R.id.etRemitente)
        val etDestinatario = view.findViewById<EditText>(R.id.etDestinatario)
        val spinnerDestino = view.findViewById<Spinner>(R.id.spinnerDestino)
        val etPeso = view.findViewById<EditText>(R.id.etPeso)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardar)
        val tvCodigo = view.findViewById<TextView>(R.id.tvCodigo)
        val tvError = view.findViewById<TextView>(R.id.tvError)

        val destinos = listOf("Selecciona destino", "Trujillo", "Angasmarca")
        spinnerDestino.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            destinos
        )

        btnGuardar.setOnClickListener {
            val destino = if (spinnerDestino.selectedItemPosition == 0) ""
            else spinnerDestino.selectedItem.toString()
            viewModel.guardarPreRegistro(
                etRemitente.text.toString().trim(),
                etDestinatario.text.toString().trim(),
                destino,
                etPeso.text.toString().trim()
            )
        }

        viewModel.preRegistro.observe(viewLifecycleOwner) { registro ->
            tvCodigo.text = "Código de reserva: #${registro.id}\nPresentar en ventanilla"
            tvCodigo.visibility = View.VISIBLE
        }

        viewModel.error.observe(viewLifecycleOwner) { mensaje ->
            tvError.text = mensaje
            tvError.visibility = if (mensaje.isEmpty()) View.GONE else View.VISIBLE
        }
    }
}