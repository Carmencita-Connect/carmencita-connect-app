# Documentacion del proyecto Carmencita Connect

Esta carpeta contiene la documentacion tecnica, funcional y de entrega del proyecto Android Kotlin **Carmencita Connect**. Su objetivo es facilitar el mantenimiento, la comprension de la arquitectura y la continuidad del desarrollo por parte del equipo.

## Estructura

```text
documentacion/
|-- arquitectura/
|-- configuracion/
|-- funcionalidades/
|-- manuales/
`-- pruebas/
```

## Contenido por carpeta

### Arquitectura

- [Arquitectura MVVM](arquitectura/arquitectura_mvvm.md): explica la separacion entre Model/Data, ViewModel, Controller y View.
- [Modulos principales](arquitectura/modulos_principales.md): describe las responsabilidades de las clases mas importantes del proyecto.
- [Flujos funcionales](arquitectura/flujos_funcionales.md): resume el comportamiento implementado para las historias de usuario principales.
- [Guia de mantenimiento](arquitectura/guia_mantenimiento.md): reune recomendaciones para realizar cambios sin romper el flujo actual.

### Configuracion

- [Configuracion de EmailJS HU11](configuracion/configuracion_emailjs_hu11.md): indica como configurar el servicio de correo para confirmacion de registro.
- [Plantilla HTML EmailJS HU11](configuracion/emailjs_template_hu11.html): plantilla usada para el correo de confirmacion.

### Funcionalidades

- [Preguntas del chatbot HU06](funcionalidades/preguntas_chatbot_hu06.md): documenta las preguntas frecuentes que responde el asistente virtual.

### Manuales

- [Manual de usuario de aplicacion](manuales/manual_usuario_aplicacion.md): explica el uso del producto y la guia de puesta en disponibilidad para el cliente.

### Pruebas

- [Pruebas HU06 Chatbot](pruebas/pruebas_hu06_chatbot.md): documenta checklist y pruebas unitarias del chatbot.
- [Pruebas HU11 Room](pruebas/pruebas_hu11_room.md): documenta pruebas de registro y perfil con Room.

## Convencion usada en el proyecto

El proyecto sigue una organizacion basada en capas:

```text
VIEW XML -> CONTROLLER -> VIEWMODEL -> MODEL / DATA
```

En esta implementacion:

- **View**: archivos XML ubicados en `app/src/main/res/layout`.
- **Controller**: `Fragment` y `DialogFragment`, ubicados en `app/src/main/java/com/carmencita/connect/ui`.
- **ViewModel**: clases ubicadas en `app/src/main/java/com/carmencita/connect/viewmodel`.
- **Model / Data**: modelos y repositorios ubicados en `model` y `data`.

## Nota de mantenimiento

La documentacion describe el estado actual del codigo. Si se agregan nuevas pantallas, repositorios, modelos o historias de usuario, se recomienda actualizar los archivos correspondientes dentro de esta carpeta.
