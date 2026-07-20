# Arquitectura MVVM

El proyecto **Carmencita Connect** organiza su código usando una variante de MVVM adaptada a la estructura de una aplicación Android Kotlin. La separación se trabaja en cuatro capas: **Model/Data**, **ViewModel**, **Controller** y **View**.

## Model / Data

Esta capa contiene las clases que representan la información del negocio y los repositorios que realizan operaciones sobre esos datos.

Responsabilidades principales:

- Representar entidades como encomiendas, pagos, personas, tarifas y comprobantes.
- Centralizar cálculos o creación de datos.
- Simular persistencia o consulta de información mediante repositorios.

Clases principales:

- `Encomienda`
- `PreRegistro`
- `Persona`
- `Pago`
- `Comprobante`
- `Tarifa`
- `Sede`
- `Notificacion`
- `AlertaCambioEstado`
- `ChatbotMessage`
- `EncomiendaRepository`
- `PreRegistroRepository`
- `PagoRepository`
- `ComprobanteRepository`
- `TarifaRepository`
- `SedeRepository`
- `NotificacionRepository`
- `AlertaEstadoRepository`
- `ChatbotTarifasService`

## ViewModel

Esta capa actúa como intermediaria entre la interfaz y los repositorios. Se encarga de validar datos, mantener el estado de las pantallas y exponer resultados mediante `LiveData`.

Responsabilidades principales:

- Validar datos ingresados por el usuario.
- Mantener estados como errores, resultados, pagos y comprobantes.
- Coordinar llamadas hacia los repositorios.
- Exponer información observable para que los fragments actualicen la interfaz.

Clases principales:

- `CotizacionViewModel`
- `PreRegistroViewModel`
- `PagoViewModel`
- `PagoPresencialViewModel`
- `ComprobanteViewModel`
- `TrackingViewModel`
- `SedesViewModel`
- `HistorialNotificacionesViewModel`
- `AlertasEstadoViewModel`
- `ChatbotTarifasViewModel`

## Controller

En este proyecto, la capa Controller está representada por los `Fragment` y `DialogFragment`. Estos componentes capturan eventos de usuario, se comunican con los ViewModel, observan cambios de estado y controlan la navegación.

Responsabilidades principales:

- Capturar acciones como botones, selección de opciones y confirmaciones.
- Enviar datos al ViewModel correspondiente.
- Observar `LiveData` para mostrar resultados o errores.
- Navegar entre pantallas.
- Mostrar diálogos de confirmación o validación.

Clases principales:

- `CotizacionFragment`
- `PreRegistroFragment`
- `PagoDigitalFragment`
- `PagoPresencialFragment`
- `PagoConfirmadoFragment`
- `TrackingFragment`
- `TrackingResultadoFragment`
- `TrackingInvalidoFragment`
- `SedesFragment`
- `HistorialNotificacionesFragment`
- `AlertasEstadoFragment`
- `ChatbotTarifasFragment`
- `ConfirmarCancelarDialog`
- `PagoValidandoDialog`
- `ComprobanteGenerandoDialog`
- `TrackingValidandoDialog`

## View

La capa View está formada por los archivos XML de layout. Estos archivos definen la estructura visual de cada pantalla o diálogo.

Responsabilidades principales:

- Definir campos de entrada, botones, textos, spinners y contenedores.
- Organizar visualmente la información mostrada al usuario.
- Servir como base para View Binding en los fragments.

Archivos principales:

- `fragment_cotizacion.xml`
- `fragment_preregistro.xml`
- `fragment_pago_digital.xml`
- `fragment_pago_presencial.xml`
- `fragment_pago_confirmado.xml`
- `fragment_comprobante_descargado.xml`
- `fragment_tracking.xml`
- `fragment_tracking_resultado.xml`
- `fragment_tracking_invalido.xml`
- `fragment_sedes.xml`
- `item_sede.xml`
- `fragment_historial_notificaciones.xml`
- `item_notificacion.xml`
- `fragment_alertas_estado.xml`
- `fragment_chatbot_tarifas.xml`
- `dialog_confirmar_cancelar.xml`
- `dialog_pago_validando.xml`
- `dialog_comprobante_generando.xml`
- `dialog_tracking_validando.xml`

## Flujo general

```text
Usuario interactúa con XML
Fragment captura la acción
ViewModel valida y coordina la operación
Repository procesa o consulta datos
ViewModel actualiza LiveData
Fragment observa cambios y actualiza la vista
```
