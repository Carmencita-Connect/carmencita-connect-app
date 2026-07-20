# Pruebas HU06 - Consulta de tarifas mediante Chatbot

## Pruebas unitarias automatizadas

Estas pruebas cubren la logica del asistente virtual implementada en
`ChatbotTarifasService`. No dependen de la interfaz Android, por lo que se
ejecutan como pruebas unitarias locales.

1.- Calculo de tarifa con datos completos

Verifica que al ingresar:

```text
cotizar largo 30 ancho 20 alto 15 peso 5 origen Trujillo destino Angasmarca
```

el chatbot retorne una tarifa estimada de `S/ 41.50`.

2.- Consulta de tarifa con datos incompletos

Verifica que al preguntar por el costo sin ingresar todos los datos, el chatbot
solicite el formato necesario para calcular la tarifa.

Mensaje usado:

```text
Cuanto cuesta mi envio?
```

3.- Consulta sobre horario de atencion

Verifica que al preguntar por el horario, el chatbot responda que la atencion
referencial es de `8:00 am a 6:00 pm`.

Mensaje usado:

```text
Cual es el horario de atencion?
```

4.- Consulta sobre tracking de encomienda

Verifica que al preguntar como rastrear una encomienda, el chatbot indique que
se debe ingresar a la opcion Tracking y escribir el numero de guia.

Mensaje usado:

```text
Como rastreo mi encomienda?
```

5.- Consulta sobre llamada a una sede

Verifica que al preguntar como llamar a una sede, el chatbot indique que se debe
entrar a Ver agencias y presionar Llamar sede.

Mensaje usado:

```text
Como llamo a una sede?
```

6.- Consulta sobre articulos restringidos

Verifica que al preguntar por articulos no permitidos, el chatbot indique que no
se deben enviar articulos peligrosos, ilegales, inflamables, dinero en efectivo
o productos no permitidos.

Mensaje usado:

```text
Que articulos no puedo enviar?
```

7.- Consulta sobre metodos de pago

Verifica que al preguntar como pagar, el chatbot responda que se puede pagar en
agencia o mediante pago digital durante el pre-registro.

Mensaje usado:

```text
Como puedo pagar?
```

8.- Validacion de origen y destino iguales

Verifica que cuando el usuario ingresa el mismo origen y destino, el chatbot
retorne un mensaje indicando que el origen y destino no pueden ser iguales.

Mensaje usado:

```text
cotizar largo 30 ancho 20 alto 15 peso 5 origen Trujillo destino Trujillo
```

## Pruebas manuales necesarias

- Ingresar como invitado y verificar que aparezca el acceso `Chatbot tarifas`.
- Iniciar sesion como usuario registrado y verificar que tambien aparezca el acceso `Chatbot tarifas`.
- Abrir el chatbot y confirmar que se muestra el mensaje inicial del asistente.
- Enviar una consulta de tarifa completa y verificar que se muestre el costo estimado dentro de la conversacion.
- Enviar una consulta incompleta y verificar que el chatbot muestre el formato requerido.
- Probar preguntas frecuentes de horarios, agencias, pagos, tracking, llamada a sedes y articulos restringidos.
- Verificar que los mensajes del usuario tengan fondo turquesa y texto legible.
- Presionar atras desde el chatbot y verificar que regrese a la pantalla anterior.

## Comandos de validacion

```powershell
.\gradlew.bat testDebugUnitTest
.\gradlew.bat assembleDebug
```
