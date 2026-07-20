# Flujos funcionales

Este documento resume los flujos principales implementados en el proyecto.

## HU01 - Cotización dinámica de servicios

El usuario ingresa las medidas del paquete, peso, origen y destino. `CotizacionFragment` captura los datos y los envía a `CotizacionViewModel`. El ViewModel valida los campos y solicita a `TarifaRepository` el cálculo de la tarifa. Si los datos son válidos, se muestra el costo estimado y se habilita la opción de realizar el pre-registro.

## HU02 - Pre-registro de encomienda

El usuario completa los datos del remitente, destinatario, descripción de carga y modo de pago. `PreRegistroFragment` envía la información al `PreRegistroViewModel`, que valida los datos y solicita a `PreRegistroRepository` guardar el pre-registro. El flujo usa la encomienda cotizada previamente desde `CotizacionViewModel`.

## HU03 - Pago digital de encomienda

El usuario accede al pago digital después de completar el pre-registro. `PagoDigitalFragment` muestra los datos del envío y permite seleccionar tarjeta o Yape. `PagoViewModel` administra el estado del pago y `PagoRepository` genera el objeto `Pago` cuando la operación se confirma.

## HU04 - Pago presencial de encomienda

El usuario accede al pago en agencia después del pre-registro. `PagoPresencialFragment` muestra los datos del envío y confirma la operación mediante `PagoPresencialViewModel`. El pago se genera a través de `PagoRepository` y luego se presenta una pantalla de confirmación.

## HU05 - Emisión de comprobantes electrónicos

Después de un pago digital confirmado, `PagoConfirmadoFragment` solicita a `ComprobanteViewModel` la generación del comprobante. `ComprobanteRepository` crea un archivo PDF con los datos del pago y del pre-registro. Al finalizar, se muestra la pantalla de comprobante descargado.

## HU07 - Tracking visual en tiempo real

El usuario ingresa un número de guía. `TrackingFragment` envía el código a `TrackingViewModel`, que consulta `EncomiendaRepository`. Si la guía existe, se muestra `TrackingResultadoFragment`; si no existe, se muestra `TrackingInvalidoFragment`. La información actual proviene de datos locales de prueba.

## HU06 - Consulta de tarifas mediante Chatbot

El usuario registrado o invitado ingresa a `ChatbotTarifasFragment` desde su menu correspondiente. La pantalla muestra una conversacion entre el usuario y el asistente virtual. `ChatbotTarifasViewModel` administra los mensajes y usa `ChatbotTarifasService` para interpretar consultas por palabras clave. Cuando el usuario ingresa largo, ancho, alto, peso, origen y destino, el servicio reutiliza `TarifaRepository` para calcular la tarifa estimada y mostrarla dentro del chat.

## HU09 - Alertas de cambio de estado

El usuario registrado ingresa a `AlertasEstadoFragment` desde el menu principal. Puede activar o desactivar alertas y simular el cambio de estado de una encomienda de prueba. `AlertasEstadoViewModel` usa `AlertaEstadoRepository` para avanzar el estado y, cuando las alertas estan activas, registra una notificacion mediante `NotificacionRepository`. La alerta tambien se muestra en pantalla y puede mostrarse como notificacion local si el permiso esta concedido.

## HU10 - Registro de historial de notificaciones

El usuario registrado ingresa a `HistorialNotificacionesFragment` desde el menu principal. `HistorialNotificacionesViewModel` consulta `NotificacionRepository`, que almacena las notificaciones en Room mediante la tabla `notificaciones`. La pantalla muestra titulo, mensaje, fecha y estado de cada notificacion. El usuario puede limpiar el historial, previa confirmacion.

## HU14 - Comunicacion rapida via llamada

El usuario ingresa al directorio de agencias desde el menu principal o modo invitado. `SedesFragment` muestra las sedes disponibles y cada tarjeta incluye el boton `Llamar sede`. Al presionarlo, se normaliza el numero mediante `FormateadorTelefonoSede` y se abre el marcador telefonico con `Intent.ACTION_DIAL`, sin realizar la llamada automaticamente.
