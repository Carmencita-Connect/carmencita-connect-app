# Manual de usuario de aplicacion - Carmencita Connect

## 1. Presentacion del producto

**Carmencita Connect** es una aplicacion movil Android orientada a clientes de una empresa de encomiendas. Su objetivo es facilitar la consulta de tarifas, el pre-registro de envios, la revision de sedes, la comunicacion con agencias, el seguimiento de cambios de estado y la gestion de informacion frecuente del usuario.

Este documento funciona como **manual de usuario de aplicacion** y como **guia para el cliente sobre como poner en disponibilidad el producto una vez hecha la entrega por el grupo de desarrollo**.

El manual esta dirigido a dos perfiles:

- **Cliente propietario del servicio**: empresa o negocio que recibira la aplicacion y la pondra a disposicion de sus usuarios.
- **Responsable tecnico del cliente**: persona encargada de configurar credenciales, compilar, publicar, integrar servicios y mantener la aplicacion.

## 2. Objetivo del manual

El objetivo de este manual es explicar de manera practica:

- Como se usa la aplicacion desde el punto de vista del usuario final.
- Que elementos debe recibir el cliente al terminar el desarrollo.
- Que configuraciones debe realizar el cliente antes de publicar la aplicacion.
- Como debe integrarse la aplicacion con la base de datos del negocio.
- Que pasos se recomiendan para publicar la aplicacion en una tienda o canal privado.
- Que debe quedar listo para que, cuando el usuario descargue la app, pueda usarla sin configuraciones tecnicas adicionales.

## 3. Alcance del MVP entregado

El producto minimo viable entregado permite demostrar los principales procesos de atencion digital de encomiendas. Las funcionalidades incluidas son:

- Registro de usuario e inicio de sesion.
- Acceso como invitado para funciones publicas.
- Cotizacion dinamica de servicios.
- Pre-registro de encomienda.
- Seleccion de metodo de pago.
- Directorio de sedes.
- Apertura de ubicacion de sede en Google Maps.
- Comunicacion rapida via llamada a una sede.
- Chatbot de tarifas y preguntas frecuentes.
- Alertas de cambio de estado para usuario registrado.
- Historial de notificaciones.
- Agenda de contactos frecuentes.

Algunas funciones del MVP usan informacion local o simulada para fines academicos y de validacion. Para produccion, el cliente debe definir servicios centrales conectados con la base de datos real del negocio.

## 4. Entrega del servicio al cliente

Al finalizar el proyecto, el grupo de desarrollo debe entregar al cliente:

| Elemento entregable | Descripcion | Responsable de recibir |
|---|---|---|
| Codigo fuente Android | Proyecto Kotlin completo con estructura MVVM, recursos XML, modelos, repositorios y pruebas. | Responsable tecnico |
| Manual de usuario de aplicacion | Documento que explica uso, configuracion y puesta en disponibilidad. | Cliente y responsable tecnico |
| Documentacion tecnica | Archivos de arquitectura, modulos, flujos y pruebas dentro de la carpeta `docs`. | Responsable tecnico |
| APK o AAB de prueba | Archivo instalable o paquete preliminar para pruebas internas. | Cliente |
| Instrucciones de configuracion | Variables necesarias para correo, compilacion y servicios externos. | Responsable tecnico |
| Checklist de puesta en disponibilidad | Lista de verificacion antes de publicar o distribuir la app. | Cliente |

El cliente debe validar que puede abrir el proyecto, compilarlo, instalarlo en un dispositivo de prueba y ejecutar los flujos principales antes de aceptar la entrega.

## 5. Uso de la aplicacion por el usuario final

### 5.1. Descarga e instalacion

Cuando la aplicacion ya este disponible en Google Play Store o en el canal definido por el cliente, el usuario final debe:

1. Ingresar a la tienda o enlace de descarga oficial.
2. Buscar o abrir la ficha de **Carmencita Connect**.
3. Presionar **Instalar**.
4. Abrir la aplicacion desde el dispositivo Android.
5. Registrarse, iniciar sesion o continuar como invitado.

El usuario final no debe configurar base de datos, credenciales, llaves ni servicios tecnicos. Todo eso debe estar preparado previamente por el cliente y su responsable tecnico.

### 5.2. Registro e inicio de sesion

El usuario puede crear una cuenta ingresando sus datos personales. Luego puede iniciar sesion para acceder a funciones asociadas a su cuenta, como agenda, alertas e historial de notificaciones.

Para que este flujo funcione en produccion, el cliente debe tener configurado:

- Servicio de autenticacion o repositorio de usuarios.
- Base de datos donde se almacenen usuarios registrados.
- Servicio de correo para confirmacion de cuenta, si se mantiene EmailJS u otro proveedor.

### 5.3. Acceso como invitado

El usuario puede ingresar como invitado para usar funciones publicas. En este modo puede revisar agencias, usar el chatbot y consultar informacion general. Las funciones que almacenan informacion personal deben quedar reservadas para usuario registrado.

### 5.4. Cotizacion de servicios

El usuario ingresa largo, ancho, alto, peso, origen y destino de la encomienda. La aplicacion muestra una tarifa aproximada.

Para produccion, el tarifario debe obtenerse desde la base de datos o API del negocio, de modo que los precios se mantengan actualizados sin modificar el codigo de la aplicacion.

### 5.5. Pre-registro de encomienda

El usuario registrado ingresa datos del remitente, destinatario, descripcion de la carga y metodo de pago. Esta informacion permite preparar el envio antes de acudir a una agencia o completar un pago digital.

Para produccion, este pre-registro debe enviarse al sistema central del negocio para que la agencia pueda consultarlo.

### 5.6. Directorio de sedes

La aplicacion muestra sedes disponibles con nombre, direccion, telefono, horario y ubicacion. En el MVP se consideran:

- Agencia Trujillo.
- Agencia Angasmarca.
- Agencia Santiago de Surco.

Para produccion, la lista de sedes debe administrarse desde la base de datos del negocio o desde un panel administrativo.

### 5.7. Comunicacion rapida via llamada

Cada sede muestra la opcion **Llamar sede**. Al presionarla, la aplicacion abre el marcador telefonico del dispositivo con el numero de la sede cargado.

La aplicacion no realiza la llamada automaticamente. El usuario decide si presiona llamar desde el marcador del dispositivo.

### 5.8. Chatbot de tarifas

El chatbot permite consultar tarifas usando un formato con datos completos, por ejemplo:

```text
cotizar largo 30 ancho 20 alto 15 peso 5 origen Trujillo destino Angasmarca
```

Tambien responde preguntas frecuentes sobre:

- Horarios de atencion.
- Agencias disponibles.
- Medios de pago.
- Tracking o seguimiento.
- Llamadas a sedes.
- Articulos restringidos.

Para produccion, el chatbot puede mantenerse con respuestas predefinidas o evolucionar hacia un servicio central que consulte tarifas, sedes y politicas actualizadas.

### 5.9. Alertas de cambio de estado

El usuario registrado puede activar o desactivar alertas. En el MVP se simula el cambio de estado de una encomienda de prueba para validar el funcionamiento.

Para produccion, los cambios de estado deben originarse en el sistema logistico real del negocio. Si se requiere recibir alertas con la aplicacion cerrada, se debe implementar un servicio de notificaciones push, por ejemplo Firebase Cloud Messaging.

### 5.10. Historial de notificaciones

El usuario registrado puede visualizar las notificaciones recibidas, ordenadas de la mas reciente a la mas antigua. En el MVP se almacenan localmente con Room.

Para produccion, el historial deberia sincronizarse con el backend del negocio para conservar trazabilidad aunque el usuario cambie de dispositivo.

## 6. Configuracion del entorno por parte del cliente

### 6.1. Herramientas necesarias

El responsable tecnico del cliente debe preparar:

- Android Studio.
- JDK compatible con el proyecto.
- Android SDK.
- Dispositivo virtual AVD o dispositivo fisico Android.
- Acceso al repositorio del proyecto.
- Acceso a credenciales de correo, firma y servicios externos.

### 6.2. Validacion local del proyecto

Antes de publicar, se recomienda ejecutar:

```bash
.\gradlew.bat assembleDebug
.\gradlew.bat testDebugUnitTest
```

Si ambas tareas terminan correctamente, el proyecto compila y las pruebas unitarias pasan.

### 6.3. Configuracion de EmailJS

Para que funcione el correo de confirmacion de registro, el cliente debe configurar en `local.properties`:

```properties
EMAILJS_SERVICE_ID=service_xxxxx
EMAILJS_TEMPLATE_ID=template_xxxxx
EMAILJS_PUBLIC_KEY=xxxxxxxxxxxx
```

Estos valores deben obtenerse desde la cuenta EmailJS del cliente. No deben subirse a GitHub ni compartirse publicamente.

### 6.4. Configuracion de firma de aplicacion

Para publicar en tienda, el cliente debe generar o custodiar una llave de firma Android. Esta llave permite crear versiones oficiales de la aplicacion.

El cliente debe guardar de forma segura:

- Archivo de keystore.
- Alias de la llave.
- Password del keystore.
- Password de la llave.

Sin esta llave no se podran publicar actualizaciones de la misma aplicacion en el futuro.

## 7. Integracion con la base de datos del negocio

### 7.1. Situacion actual del MVP

El MVP utiliza Room para almacenamiento local en el dispositivo. Esto permite guardar datos como usuarios, contactos frecuentes y notificaciones durante las pruebas.

Room es adecuado para persistencia local, pero no reemplaza la base de datos central del negocio cuando se necesita informacion compartida entre agencias, usuarios y dispositivos.

### 7.2. Integracion recomendada

Para que la aplicacion funcione como producto real, el cliente debe conectar la app con un backend. La arquitectura recomendada es:

```text
Aplicacion Android -> API segura del negocio -> Base de datos del negocio
```

La aplicacion no debe conectarse directamente a la base de datos empresarial. La conexion debe realizarse mediante endpoints seguros.

### 7.3. Informacion que deberia venir del negocio

| Modulo | Informacion requerida desde el negocio | Motivo |
|---|---|---|
| Usuarios | Registro, login, perfil y sesion | Identificar al cliente y proteger datos personales |
| Tarifas | Precios vigentes, origen, destino, peso, volumen | Cotizar con informacion real y actualizada |
| Sedes | Direccion, telefono, horario, ubicacion | Mantener agencias actualizadas sin modificar la app |
| Encomiendas | Datos del envio y pre-registro | Preparar operaciones en agencia |
| Tracking | Estado real del envio | Informar avance logistico |
| Notificaciones | Cambios de estado y alertas enviadas | Mantener historial y trazabilidad |
| Pagos | Estado de pago, metodo y comprobante | Confirmar operaciones economicas |

### 7.4. Configuracion necesaria para que el usuario descargue y funcione

Antes de publicar la app, el cliente debe asegurarse de que:

1. La API de produccion este disponible.
2. La aplicacion apunte a la URL correcta del ambiente de produccion.
3. Las credenciales externas esten configuradas fuera del codigo fuente.
4. El backend tenga datos iniciales de tarifas, sedes y estados.
5. La base de datos del negocio tenga tablas o servicios para usuarios, encomiendas, tracking y notificaciones.
6. Las politicas de seguridad y privacidad esten aprobadas.
7. La aplicacion haya sido probada con datos reales o datos de prueba del negocio.

Si estos puntos estan listos, el usuario final solo descargara la aplicacion y podra usarla sin configuracion manual.

## 8. Puesta en disponibilidad en tienda o canal privado

### 8.1. Publicacion en Google Play Store

Si el cliente desea publicar en Google Play Store, debe preparar:

- Cuenta de Google Play Console.
- Nombre de la aplicacion.
- Icono oficial.
- Capturas de pantalla.
- Descripcion corta y descripcion completa.
- Categoria de la aplicacion.
- Politica de privacidad.
- Correo o canal de soporte.
- Archivo `.aab` firmado.
- Revision de permisos solicitados por la aplicacion.

Luego debe subir el paquete, completar la ficha de tienda, enviar a revision y esperar aprobacion de Google.

### 8.2. Distribucion privada

Si el cliente no desea publicar en tienda, puede distribuir la aplicacion por:

- APK interno para pruebas.
- Canal empresarial privado.
- Prueba cerrada en Google Play.

Para distribucion privada se recomienda controlar que solo usuarios autorizados reciban el instalador.

## 9. Checklist de puesta en disponibilidad

| Nro. | Item a verificar | Responsable | Estado sugerido |
|---|---|---|---|
| 1 | El codigo fuente fue entregado al cliente. | Equipo de desarrollo | Obligatorio |
| 2 | El proyecto compila correctamente en Android Studio. | Responsable tecnico | Obligatorio |
| 3 | Las pruebas unitarias pasan correctamente. | Responsable tecnico | Obligatorio |
| 4 | La aplicacion fue probada en emulador Android. | Equipo de desarrollo | Obligatorio |
| 5 | La aplicacion fue probada en dispositivo fisico. | Cliente | Recomendado |
| 6 | EmailJS u otro servicio de correo fue configurado. | Cliente | Obligatorio si se usa confirmacion |
| 7 | La llave de firma de produccion fue generada y custodiada. | Cliente | Obligatorio |
| 8 | La ficha de tienda fue preparada. | Cliente | Obligatorio si se publica |
| 9 | La politica de privacidad fue definida. | Cliente | Obligatorio |
| 10 | Se definio el backend o API del negocio. | Cliente | Obligatorio para produccion |
| 11 | Se definio la integracion con la base de datos del negocio. | Cliente | Obligatorio para produccion |
| 12 | Las tarifas reales fueron cargadas en el sistema del negocio. | Cliente | Obligatorio para produccion |
| 13 | Las sedes reales fueron cargadas en el sistema del negocio. | Cliente | Obligatorio para produccion |
| 14 | Se definio el canal de soporte al usuario final. | Cliente | Recomendado |
| 15 | Se definio un responsable de mantenimiento. | Cliente | Recomendado |

## 10. Limitaciones actuales del MVP

El MVP permite demostrar el funcionamiento general, pero antes de una publicacion real deben evaluarse estas limitaciones:

- Algunas tarifas y sedes estan definidas dentro del aplicativo.
- El historial se almacena localmente con Room.
- Las alertas de estado usan simulacion local.
- No existe todavia un backend central conectado a la base de datos real del negocio.
- Las notificaciones con la aplicacion cerrada requieren una implementacion push adicional.
- Los datos administrables del negocio deberian migrarse a servicios externos o API.

Estas limitaciones no invalidan el MVP. Sirven para diferenciar entre la version academica funcional y una version productiva conectada al negocio.

## 11. Recomendaciones de mantenimiento

Para mantener la aplicacion despues de la entrega, se recomienda:

- No guardar credenciales dentro del codigo fuente.
- Actualizar la documentacion cuando se agreguen nuevas historias de usuario.
- Mantener pruebas unitarias para reglas de negocio.
- Usar ramas separadas para nuevas funcionalidades.
- Validar cada version antes de publicarla.
- Centralizar tarifas, sedes y tracking en servicios del negocio.
- Realizar copias de seguridad de llaves de firma y configuraciones.

## 12. Cierre de entrega

Una vez configurado el entorno, validada la compilacion, preparada la integracion con el negocio y definido el canal de publicacion, el cliente puede poner en disponibilidad la aplicacion para sus usuarios.

El resultado esperado es que el usuario final descargue **Carmencita Connect**, abra la aplicacion y pueda registrarse, cotizar, consultar sedes, comunicarse con agencias, pre-registrar encomiendas y revisar informacion de seguimiento sin realizar configuraciones tecnicas adicionales.
