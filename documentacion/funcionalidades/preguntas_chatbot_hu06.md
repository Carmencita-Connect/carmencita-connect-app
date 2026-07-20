# HU06 - Preguntas del chatbot de tarifas

Este documento lista las consultas que el chatbot de tarifas puede responder en el MVP.
El asistente funciona mediante palabras clave y respuestas predefinidas, reutilizando la
logica de cotizacion del aplicativo cuando el usuario ingresa los datos completos del envio.

## Consulta de tarifa

Ejemplo recomendado:

```text
cotizar largo 30 ancho 20 alto 15 peso 5 origen Trujillo destino Angasmarca
```

Respuesta esperada:

```text
La tarifa estimada para enviar de Trujillo a Angasmarca es S/ 41.50. Este monto es referencial y puede confirmarse en el pre-registro.
```

Variaciones aceptadas:

- `quiero cotizar`
- `cuanto cuesta mi envio`
- `tarifa de envio`

Si faltan datos, el chatbot indica el formato correcto para realizar la cotizacion.

## Preguntas frecuentes

| Tema | Preguntas de ejemplo | Respuesta esperada |
|---|---|---|
| Horarios | `Cual es el horario de atencion?`, `A que hora atienden?` | Informa que el horario referencial es Lun - Dom de 8:00 am a 6:00 pm. |
| Agencias | `Donde estan las agencias?`, `Que sedes tienen?`, `ubicacion de agencias` | Indica que se puede revisar la opcion Ver agencias y menciona Trujillo, Angasmarca y Santiago de Surco. |
| Pagos | `Como puedo pagar?`, `Aceptan Yape?`, `Puedo pagar online?`, `Pago con tarjeta?` | Indica que se puede pagar en agencia o mediante pago digital durante el pre-registro. |
| Tracking | `Como rastreo mi encomienda?`, `Como hago tracking?`, `Donde ingreso mi guia?` | Indica que debe ingresar a Tracking y escribir su numero de guia. |
| Llamada a sede | `Como llamo a una sede?`, `Telefono de agencia`, `Quiero contactar una sede` | Indica que debe entrar a Ver agencias y presionar Llamar sede. |
| Datos para enviar | `Que datos necesito para enviar?`, `Datos para una encomienda` | Indica que se necesita nombre, DNI, telefono y direccion del remitente y destinatario, ademas de la descripcion de la carga. |
| Articulos restringidos | `Que articulos no puedo enviar?`, `Productos prohibidos`, `Articulos peligrosos` | Indica que no se deben enviar articulos peligrosos, ilegales, inflamables, dinero en efectivo o productos no permitidos. |

