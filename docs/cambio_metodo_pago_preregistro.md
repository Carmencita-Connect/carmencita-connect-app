# Cambio en `metodoPago` dentro de `PreRegistroViewModel`

## Problema

En `PreRegistroViewModel`, el parámetro `metodoPago` de la función `guardarPreRegistro(...)` aparecía en gris porque no se estaba usando dentro de la función.

El código tenía algo así:

```kotlin
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
    metodoPago: String,
    encomienda: Encomienda?
)
```

Pero dentro de la función no se usaba ese parámetro. En realidad, el ViewModel ya tenía su propio estado:

```kotlin
private val _metodoPago = MutableLiveData<String>("")
val metodoPago: LiveData<String> = _metodoPago
```

Y el método de pago se selecciona con:

```kotlin
fun seleccionarMetodoPago(metodo: String) {
    _metodoPago.value = metodo
}
```

## Solución

Se eliminó el parámetro `metodoPago` de `guardarPreRegistro(...)`, porque era redundante.

El método de pago **no se eliminó del ViewModel**. Solo se eliminó el parámetro que no se usaba.

## Archivo modificado

```text
app/src/main/java/com/carmencita/connect/viewmodel/PreRegistroViewModel.kt
```

La función debe quedar así:

```kotlin
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
        _error.value = "Primero realiza una cotización válida"
        return
    }

    // El resto de validaciones y guardado se mantiene igual.
}
```

## Archivo modificado

```text
app/src/main/java/com/carmencita/connect/ui/preregistro/PreRegistroFragment.kt
```

En la llamada a `guardarPreRegistro(...)`, se eliminó esta línea:

```kotlin
metodoPago = viewModel.metodoPago.value ?: "",
```

La llamada debe quedar así:

```kotlin
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
```

## Importante

No se debe eliminar esto de `PreRegistroViewModel`:

```kotlin
private val _metodoPago = MutableLiveData<String>("")
val metodoPago: LiveData<String> = _metodoPago

fun seleccionarMetodoPago(metodo: String) {
    _metodoPago.value = metodo
}
```

Eso sí se usa para saber si el usuario eligió:

```text
agencia
digital
```

Y también se usa en `PreRegistroFragment` para decidir a qué pantalla navegar después de guardar el pre-registro.

## Resumen

```text
Se elimina solo el parámetro metodoPago de guardarPreRegistro(...)
Se mantiene metodoPago como LiveData dentro de PreRegistroViewModel
Se mantiene seleccionarMetodoPago(...)
El flujo de pago digital y pago en agencia sigue funcionando
```
