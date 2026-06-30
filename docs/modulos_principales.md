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

## Pantalla de invitado

Componentes:

- `activity_main.xml`
- `MainActivity`
- `fragment_invitado.xml`
- `InvitadoFragment`

Responsabilidad:

Muestra el menú inicial para acceder a cotización, tracking o llamada telefónica.
