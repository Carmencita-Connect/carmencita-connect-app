# Codigo completo de archivos modificados

## app/src/main/java/com/carmencita/connect/data/EncomiendaRepository.kt

```kotlin
package com.carmencita.connect.data

import com.carmencita.connect.model.Encomienda
import com.carmencita.connect.model.Tarifa

class EncomiendaRepository {

    private val encomiendas = listOf(
        Encomienda(
            id           = 1,
            numeroGuia   = "C000000001",
            largo        = 20.0,
            ancho        = 20.0,
            alto         = 20.0,
            peso         = 10.0,
            origen       = "Trujillo",
            destino      = "Angasmarca",
            tarifa       = Tarifa(destino = "Angasmarca", costo = 45.0),
            estado       = "EN AGENCIA",
            fechaRegistro = "20/05/26",
            fechaTransito = "20/05/26",
            fechaAgencia  = "21/05/26",
            fechaEntrega  = null
        )
    )

    fun crearCotizada(
        largo: Double,
        ancho: Double,
        alto: Double,
        peso: Double,
        origen: String,
        destino: String,
        tarifa: Tarifa
    ): Encomienda {
        return Encomienda(
            largo = largo,
            ancho = ancho,
            alto = alto,
            peso = peso,
            origen = origen,
            destino = destino,
            tarifa = tarifa,
            estado = "pendiente"
        )
    }

    fun buscarPorGuia(numeroGuia: String): Encomienda? {
        return encomiendas.find {
            it.numeroGuia.equals(numeroGuia.trim(), ignoreCase = true)
        }
    }
}
```

## app/src/main/java/com/carmencita/connect/viewmodel/CotizacionViewModel.kt

```kotlin
package com.carmencita.connect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carmencita.connect.data.EncomiendaRepository
import com.carmencita.connect.data.TarifaRepository
import com.carmencita.connect.model.Encomienda
import com.carmencita.connect.model.Tarifa

class CotizacionViewModel : ViewModel() {

    private val tarifaRepository = TarifaRepository()
    private val encomiendaRepository = EncomiendaRepository()

    private val _costoEstimado = MutableLiveData<Double>()
    val costoEstimado: LiveData<Double> = _costoEstimado

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Datos expuestos para PreRegistroFragment
    private val _destinoSeleccionado = MutableLiveData<String>()
    val destinoSeleccionado: LiveData<String> = _destinoSeleccionado

    private val _largo = MutableLiveData<Double>()
    val largo: LiveData<Double> = _largo

    private val _ancho = MutableLiveData<Double>()
    val ancho: LiveData<Double> = _ancho

    private val _alto = MutableLiveData<Double>()
    val alto: LiveData<Double> = _alto

    private val _peso = MutableLiveData<Double>()
    val peso: LiveData<Double> = _peso

    private val _tarifa = MutableLiveData<Tarifa>()
    val tarifa: LiveData<Tarifa> = _tarifa

    private val _encomiendaCotizada = MutableLiveData<Encomienda?>()
    val encomiendaCotizada: LiveData<Encomienda?> = _encomiendaCotizada

    fun calcularTarifa(
        largo: String,
        ancho: String,
        alto: String,
        peso: String,
        origen: String,
        destino: String
    ) {
        if (largo.isEmpty() || ancho.isEmpty() ||
            alto.isEmpty() || peso.isEmpty()) {
            _error.value = "Completa todas las medidas"
            return
        }

        val largoD = largo.toDoubleOrNull()
        val anchoD = ancho.toDoubleOrNull()
        val altoD  = alto.toDoubleOrNull()
        val pesoD  = peso.toDoubleOrNull()

        if (largoD == null || largoD <= 0 ||
            anchoD == null || anchoD <= 0 ||
            altoD  == null || altoD  <= 0) {
            _error.value = "Las medidas deben ser nÃºmeros positivos"
            return
        }

        if (pesoD == null || pesoD <= 0) {
            _error.value = "El peso debe ser un nÃºmero positivo"
            return
        }

        if (origen == destino) {
            _error.value = "El origen y destino no pueden ser iguales"
            return
        }

        _largo.value = largoD
        _ancho.value = anchoD
        _alto.value  = altoD
        _peso.value  = pesoD
        _destinoSeleccionado.value = destino

        val tarifa = tarifaRepository.calcularTarifa(largoD, anchoD, altoD, pesoD, destino)
        val encomienda = encomiendaRepository.crearCotizada(
            largo = largoD,
            ancho = anchoD,
            alto = altoD,
            peso = pesoD,
            origen = origen,
            destino = destino,
            tarifa = tarifa
        )
        _error.value = ""
        _tarifa.value = tarifa
        _costoEstimado.value = tarifa.costo
        _encomiendaCotizada.value = encomienda
    }

    fun resetear() {
        _costoEstimado.value = 0.0
        _error.value = ""
        _destinoSeleccionado.value = ""
        _largo.value = 0.0
        _ancho.value = 0.0
        _alto.value = 0.0
        _peso.value = 0.0
        _tarifa.value = Tarifa()
        _encomiendaCotizada.value = null
    }
}
```

## app/src/main/java/com/carmencita/connect/ui/preregistro/PreRegistroFragment.kt

```kotlin
package com.carmencita.connect.ui.preregistro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carmencita.connect.R
import com.carmencita.connect.databinding.FragmentPreregistroBinding
import com.carmencita.connect.viewmodel.CotizacionViewModel
import com.carmencita.connect.viewmodel.PagoViewModel
import com.carmencita.connect.viewmodel.PreRegistroViewModel

class PreRegistroFragment : Fragment(), ConfirmarCancelarDialog.Listener {

    private var _binding: FragmentPreregistroBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PreRegistroViewModel by activityViewModels()
    private val cotizacionViewModel: CotizacionViewModel by activityViewModels()
    private val pagoViewModel: PagoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreregistroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPagoAgencia.setOnClickListener {
            viewModel.seleccionarMetodoPago("agencia")
        }

        binding.btnPagoDigital.setOnClickListener {
            viewModel.seleccionarMetodoPago("digital")
        }

        binding.btnContinuar.setOnClickListener {
            val imm = requireContext().getSystemService(
                android.content.Context.INPUT_METHOD_SERVICE
            ) as android.view.inputmethod.InputMethodManager
            imm.hideSoftInputFromWindow(binding.etRemitente.windowToken, 0)

            val encomienda = cotizacionViewModel.encomiendaCotizada.value

            viewModel.guardarPreRegistro(
                nombreRemitente       = binding.etRemitente.text.toString().trim(),
                dniRemitente          = binding.etDniRemitente.text.toString().trim(),
                telefonoRemitente     = binding.etTelefonoRemitente.text.toString().trim(),
                direccionRemitente    = binding.etDireccionRemitente.text.toString().trim(),
                nombreDestinatario    = binding.etDestinatario.text.toString().trim(),
                dniDestinatario       = binding.etDniDestinatario.text.toString().trim(),
                telefonoDestinatario  = binding.etTelefonoDestinatario.text.toString().trim(),
                direccionDestinatario = binding.etDireccionDestinatario.text.toString().trim(),
                descripcionCarga      = binding.etDescripcionCarga.text.toString().trim(),
                encomienda            = encomienda
            )
        }

        binding.btnEliminar.setOnClickListener {
            ConfirmarCancelarDialog()
                .show(childFragmentManager, "ConfirmarCancelarDialog")
        }

        viewModel.metodoPago.observe(viewLifecycleOwner) { metodo ->
            when (metodo) {
                "agencia" -> {
                    binding.btnPagoAgencia.setBackgroundResource(R.drawable.bg_boton_seleccionado)
                    binding.btnPagoDigital.setBackgroundResource(R.drawable.bg_boton_blanco)
                }
                "digital" -> {
                    binding.btnPagoDigital.setBackgroundResource(R.drawable.bg_boton_seleccionado)
                    binding.btnPagoAgencia.setBackgroundResource(R.drawable.bg_boton_blanco)
                }
                else -> {
                    binding.btnPagoAgencia.setBackgroundResource(R.drawable.bg_boton_blanco)
                    binding.btnPagoDigital.setBackgroundResource(R.drawable.bg_boton_blanco)
                }
            }
        }

        viewModel.preRegistroGuardado.observe(viewLifecycleOwner) { preRegistro ->
            preRegistro ?: return@observe
            when (viewModel.metodoPago.value) {
                "digital" -> {
                    pagoViewModel.resetear()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.contenedorFragment,
                            com.carmencita.connect.ui.pago.PagoDigitalFragment())
                        .addToBackStack(null)
                        .commit()
                }
                "agencia" -> {
                    pagoViewModel.resetear()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.contenedorFragment,
                            com.carmencita.connect.ui.pago.PagoPresencialFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { mensaje ->
            if (mensaje.isNotEmpty()) {
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = mensaje
            } else {
                binding.tvError.visibility = View.GONE
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : androidx.activity.OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.resetear()
                    cotizacionViewModel.resetear()
                    parentFragmentManager.popBackStack()
                }
            }
        )
    }

    override fun onConfirmarCancelacion() {
        viewModel.resetear()
        cotizacionViewModel.resetear()
        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
```

## app/src/main/java/com/carmencita/connect/viewmodel/PreRegistroViewModel.kt

```kotlin
package com.carmencita.connect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carmencita.connect.data.PreRegistroRepository
import com.carmencita.connect.model.Encomienda
import com.carmencita.connect.model.Persona
import com.carmencita.connect.model.PreRegistro

class PreRegistroViewModel : ViewModel() {

    private val repository = PreRegistroRepository()

    private val _preRegistroGuardado = MutableLiveData<PreRegistro?>()
    val preRegistroGuardado: LiveData<PreRegistro?> = _preRegistroGuardado

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _eliminado = MutableLiveData<Boolean>()

    private val _metodoPago = MutableLiveData<String>("")
    val metodoPago: LiveData<String> = _metodoPago

    fun seleccionarMetodoPago(metodo: String) {
        _metodoPago.value = metodo
    }

    fun limpiarPreRegistroGuardado() {
        _preRegistroGuardado.value = null
    }

    fun guardarPreRegistro(
        nombreRemitente: String,
        dniRemitente: String,
        telefonoRemitente: String,
        direccionRemitente: String,
        nombreDestinatario: String,
        dniDestinatario: String,
        telefonoDestinatario: String,
        direccionDestinatario: String,
        descripcionCarga: String,
        encomienda: Encomienda?
    ) {
        if (nombreRemitente.isEmpty() || nombreDestinatario.isEmpty()) {
            _error.value = "Completa el nombre del remitente y destinatario"
            return
        }
        if (dniRemitente.isEmpty() || dniDestinatario.isEmpty()) {
            _error.value = "Completa el DNI del remitente y destinatario"
            return
        }
        if (telefonoRemitente.isEmpty() || telefonoDestinatario.isEmpty()) {
            _error.value = "Completa el telÃ©fono del remitente y destinatario"
            return
        }
        if (direccionRemitente.isEmpty() || direccionDestinatario.isEmpty()) {
            _error.value = "Completa la direcciÃ³n del remitente y destinatario"
            return
        }
        if (descripcionCarga.isEmpty()) {
            _error.value = "Ingresa una descripciÃ³n de la carga"
            return
        }
        if (!nombreRemitente.all { it.isLetter() || it.isWhitespace() }) {
            _error.value = "El nombre del remitente solo debe contener letras"
            return
        }
        if (!nombreDestinatario.all { it.isLetter() || it.isWhitespace() }) {
            _error.value = "El nombre del destinatario solo debe contener letras"
            return
        }
        if (dniRemitente.length != 8 || !dniRemitente.all { it.isDigit() }) {
            _error.value = "El DNI del remitente debe tener 8 dÃ­gitos"
            return
        }
        if (dniDestinatario.length != 8 || !dniDestinatario.all { it.isDigit() }) {
            _error.value = "El DNI del destinatario debe tener 8 dÃ­gitos"
            return
        }
        if (telefonoRemitente.length != 9 || !telefonoRemitente.all { it.isDigit() }) {
            _error.value = "El telÃ©fono del remitente debe tener 9 dÃ­gitos"
            return
        }
        if (telefonoDestinatario.length != 9 || !telefonoDestinatario.all { it.isDigit() }) {
            _error.value = "El telÃ©fono del destinatario debe tener 9 dÃ­gitos"
            return
        }
        val metodo = _metodoPago.value ?: ""
        if (metodo.isEmpty()) {
            _error.value = "Selecciona un modo de pago"
            return
        }
        if (encomienda == null ||
            encomienda.largo <= 0 ||
            encomienda.ancho <= 0 ||
            encomienda.alto <= 0 ||
            encomienda.peso <= 0
        ) {
            _error.value = "Primero realiza una cotizacion valida"
            return
        }

        val remitente = Persona(
            nombre    = nombreRemitente,
            dni       = dniRemitente,
            telefono  = telefonoRemitente,
            direccion = direccionRemitente
        )

        val destinatario = Persona(
            nombre    = nombreDestinatario,
            dni       = dniDestinatario,
            telefono  = telefonoDestinatario,
            direccion = direccionDestinatario
        )

        val guardado = repository.guardar(
            remitente        = remitente,
            destinatario     = destinatario,
            descripcionCarga = descripcionCarga,
            encomienda       = encomienda
        )
        _error.value = ""
        _preRegistroGuardado.value = guardado
    }

    fun marcarComoPagado() {
        val actual = _preRegistroGuardado.value ?: return
        _preRegistroGuardado.value = actual.copy(estado = "pagado")
    }

    fun resetear() {
        _preRegistroGuardado.value = null
        _metodoPago.value = ""
        _error.value = ""
        _eliminado.value = false
    }
}
```

## app/src/main/java/com/carmencita/connect/data/PreRegistroRepository.kt

```kotlin
package com.carmencita.connect.data

import com.carmencita.connect.model.Encomienda
import com.carmencita.connect.model.Persona
import com.carmencita.connect.model.PreRegistro
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PreRegistroRepository {

    private val preRegistros = mutableListOf<PreRegistro>()

    fun guardar(
        remitente: Persona,
        destinatario: Persona,
        descripcionCarga: String,
        encomienda: Encomienda
    ): PreRegistro {
        val nuevo = PreRegistro(
            id               = (1000..9999).random(),
            remitente        = remitente,
            destinatario     = destinatario,
            descripcionCarga = descripcionCarga,
            encomienda       = encomienda,
            estado           = "pendiente",
            fechaCreacion    = SimpleDateFormat(
                "dd/MM/yy", Locale.getDefault()
            ).format(Date())
        )
        preRegistros.add(nuevo)
        return nuevo
    }

    fun eliminar(id: Int) {
        preRegistros.removeAll { it.id == id }
    }

    fun obtenerTodos(): List<PreRegistro> {
        return preRegistros.toList()
    }
}
```
