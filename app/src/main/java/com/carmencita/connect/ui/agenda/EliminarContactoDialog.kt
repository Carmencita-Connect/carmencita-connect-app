package com.carmencita.connect.ui.agenda

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.carmencita.connect.databinding.DialogEliminarContactoBinding

class EliminarContactoDialog : DialogFragment() {

    interface Listener {
        fun onConfirmarEliminarContacto(dni: String)
    }

    private var _binding: DialogEliminarContactoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogEliminarContactoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (320 * resources.displayMetrics.density).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dni = requireArguments().getString(ARG_DNI).orEmpty()
        binding.tvNombreContactoEliminar.text = requireArguments().getString(ARG_NOMBRE).orEmpty()

        binding.btnSiEliminar.setOnClickListener {
            dismiss()
            (parentFragment as? Listener)?.onConfirmarEliminarContacto(dni)
        }

        binding.btnNoEliminar.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_DNI = "dni"
        private const val ARG_NOMBRE = "nombre"

        fun newInstance(dni: String, nombre: String): EliminarContactoDialog {
            return EliminarContactoDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_DNI, dni)
                    putString(ARG_NOMBRE, nombre)
                }
            }
        }
    }
}
