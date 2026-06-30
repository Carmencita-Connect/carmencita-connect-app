package com.carmencita.connect.ui.preregistro

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.carmencita.connect.databinding.DialogConfirmarCancelarBinding

class ConfirmarCancelarDialog : DialogFragment() {

    interface Listener {
        fun onConfirmarCancelacion()
    }

    private var _binding: DialogConfirmarCancelarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogConfirmarCancelarBinding.inflate(inflater, container, false)
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
            (300 * resources.displayMetrics.density).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNoVolver.setOnClickListener {
            dismiss()
        }

        binding.btnSiCancelar.setOnClickListener {
            dismiss()
            (parentFragment as? Listener)?.onConfirmarCancelacion()
                ?: (activity as? Listener)?.onConfirmarCancelacion()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}