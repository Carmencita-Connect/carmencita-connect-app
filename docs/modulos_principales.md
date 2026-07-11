# Módulos principales

Este documento resume los módulos implementados en la aplicación y las responsabilidades de sus componentes.

## Cotización

Componentes:

- `fragment_cotizacion.xml`
- `CotizacionFragment`
- `CotizacionViewModel`
- `TarifaRepository`
- `EncomiendaRepository`
- `Tarifa`
- `Encomienda`

Responsabilidad:

Permite ingresar largo, ancho, alto, peso, origen y destino para calcular una tarifa estimada. El cálculo se realiza en `TarifaRepository`. Luego, `EncomiendaRepository` crea una encomienda cotizada que será reutilizada por el pre-registro.

## Pre-registro

Componentes:

- `fragment_preregistro.xml`
- `PreRegistroFragment`
- `PreRegistroViewModel`
- `PreRegistroRepository`
- `ConfirmarCancelarDialog`
- `dialog_confirmar_cancelar.xml`
- `Persona`
- `PreRegistro`
- `Encomienda`

Responsabilidad:

Permite registrar datos del remitente, destinatario, descripción de carga y modo de pago. El ViewModel valida los campos obligatorios y el repositorio crea el objeto `PreRegistro` con la encomienda ya cotizada.

## Pago digital

Componentes:

- `fragment_pago_digital.xml`
- `PagoDigitalFragment`
- `PagoViewModel`
- `PagoRepository`
- `PagoValidandoDialog`
- `dialog_pago_validando.xml`
- `Pago`

Responsabilidad:

Permite seleccionar un método de pago digital, como tarjeta o Yape. El ViewModel mantiene el estado del pago y el repositorio genera el objeto `Pago` cuando la operación se confirma.

## Pago presencial

Componentes:

- `fragment_pago_presencial.xml`
- `PagoPresencialFragment`
- `PagoPresencialViewModel`
- `PagoRepository`
- `PagoPresencialConfirmadoFragment`
- `fragment_pago_presencial_confirmado.xml`

Responsabilidad:

Permite confirmar un pago en agencia. El ViewModel solicita al repositorio la creación del pago y luego se muestra una pantalla de confirmación.

## Comprobantes

Componentes:

- `fragment_pago_confirmado.xml`
- `PagoConfirmadoFragment`
- `ComprobanteViewModel`
- `ComprobanteRepository`
- `ComprobanteGenerandoDialog`
- `dialog_comprobante_generando.xml`
- `ComprobanteDescargadoFragment`
- `fragment_comprobante_descargado.xml`
- `Comprobante`

Responsabilidad:

Genera un comprobante en PDF después de un pago digital confirmado. El repositorio construye el documento usando la información del pago y del pre-registro.

## Tracking

Componentes:

- `fragment_tracking.xml`
- `TrackingFragment`
- `TrackingViewModel`
- `EncomiendaRepository`
- `TrackingResultadoFragment`
- `fragment_tracking_resultado.xml`
- `TrackingInvalidoFragment`
- `fragment_tracking_invalido.xml`
- `TrackingValidandoDialog`
- `dialog_tracking_validando.xml`

Responsabilidad:

Permite buscar una encomienda por número de guía. Si la guía existe, se muestra el estado y la ruta logística. Si no existe, se muestra una pantalla de error.

## Chatbot de tarifas

Componentes:

- `fragment_chatbot_tarifas.xml`
- `ChatbotTarifasFragment`
- `ChatbotTarifasViewModel`
- `ChatbotTarifasAssistant`
- `ChatbotMessage`
- `TarifaRepository`

Responsabilidad:

Permite consultar tarifas aproximadas y preguntas frecuentes desde modo invitado o usuario registrado. El asistente interpreta consultas por palabras clave y reutiliza `TarifaRepository` cuando recibe largo, ancho, alto, peso, origen y destino.

## Historial de notificaciones

Componentes:

- `fragment_historial_notificaciones.xml`
- `item_notificacion.xml`
- `HistorialNotificacionesFragment`
- `HistorialNotificacionesViewModel`
- `NotificacionRepository`
- `NotificacionDao`
- `NotificacionEntity`
- `Notificacion`

Responsabilidad:

Permite al usuario registrado revisar las notificaciones recibidas. La informacion se almacena en Room en la tabla `notificaciones`, asociada al `personaId` del usuario.

## Alertas de estado

Componentes:

- `fragment_alertas_estado.xml`
- `AlertasEstadoFragment`
- `AlertasEstadoViewModel`
- `AlertaEstadoRepository`
- `AlertaEstadoPolicy`
- `AlertaLocalNotifier`
- `AlertaCambioEstado`
- `NotificacionRepository`

Responsabilidad:

Permite activar o desactivar alertas, simular cambios de estado de una encomienda de prueba y registrar una notificacion en el historial cuando las alertas estan activas.

## Directorio de sedes y llamada rapida

Componentes:

- `fragment_sedes.xml`
- `item_sede.xml`
- `SedesFragment`
- `SedesViewModel`
- `SedeRepository`
- `TelefonoSedeFormatter`
- `Sede`

Responsabilidad:

Muestra las sedes disponibles con direccion, horario y telefono. Cada sede permite abrir su ubicacion y usar el boton `Llamar sede`, que abre el marcador telefonico con el numero fijo correspondiente.

## Pantalla de invitado

Componentes:

- `activity_main.xml`
- `MainActivity`
- `fragment_invitado.xml`
- `InvitadoFragment`
- `ChatbotTarifasFragment`

Responsabilidad:

Muestra el menú inicial para acceder a cotización, tracking o llamada telefónica.
