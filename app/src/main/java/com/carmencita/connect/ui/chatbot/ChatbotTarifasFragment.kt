package com.carmencita.connect.ui.chatbot

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.carmencita.connect.R
import com.carmencita.connect.databinding.FragmentChatbotTarifasBinding
import com.carmencita.connect.model.ChatbotMessage
import com.carmencita.connect.viewmodel.ChatbotTarifasViewModel

class ChatbotTarifasFragment : Fragment() {

    private var _binding: FragmentChatbotTarifasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatbotTarifasViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatbotTarifasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEnviarChatbot.setOnClickListener {
            enviarMensaje()
        }

        binding.etMensajeChatbot.setOnEditorActionListener { _, _, _ ->
            enviarMensaje()
            true
        }

        viewModel.mensajes.observe(viewLifecycleOwner, ::mostrarMensajes)
    }

    private fun enviarMensaje() {
        val texto = binding.etMensajeChatbot.text.toString()
        viewModel.enviarMensaje(texto)
        binding.etMensajeChatbot.setText("")
        ocultarTeclado()
    }

    private fun mostrarMensajes(mensajes: List<ChatbotMessage>) {
        binding.contenedorMensajesChatbot.removeAllViews()
        mensajes.forEach { mensaje ->
            binding.contenedorMensajesChatbot.addView(crearBurbuja(mensaje))
        }
        binding.scrollChatbot.post {
            binding.scrollChatbot.fullScroll(View.FOCUS_DOWN)
        }
    }

    private fun crearBurbuja(mensaje: ChatbotMessage): TextView {
        val margenHorizontal = resources.getDimensionPixelSize(R.dimen.chatbot_bubble_margin)
        val padding = resources.getDimensionPixelSize(R.dimen.chatbot_bubble_padding)

        return TextView(requireContext()).apply {
            text = mensaje.texto
            textSize = 14f
            typeface = Typeface.DEFAULT
            setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (mensaje.enviadoPorUsuario) R.color.white else R.color.color_letra_2
                )
            )
            setBackgroundResource(
                if (mensaje.enviadoPorUsuario) R.drawable.bg_chatbot_usuario
                else R.drawable.bg_campo_texto
            )
            setPadding(padding, padding, padding, padding)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(margenHorizontal, 6, margenHorizontal, 6)
                gravity = if (mensaje.enviadoPorUsuario) Gravity.END else Gravity.START
            }
            maxWidth = resources.displayMetrics.widthPixels -
                resources.getDimensionPixelSize(R.dimen.chatbot_bubble_max_offset)
            gravity = Gravity.CENTER_VERTICAL
            textAlignment = View.TEXT_ALIGNMENT_TEXT_START
            if (mensaje.enviadoPorUsuario) {
                this.gravity = Gravity.CENTER_VERTICAL
            }
        }
    }

    private fun ocultarTeclado() {
        val inputMethodManager = requireContext().getSystemService(
            android.content.Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.etMensajeChatbot.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
