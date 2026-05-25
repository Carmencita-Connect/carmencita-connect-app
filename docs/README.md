# Documentación del proyecto Carmencita Connect

Esta carpeta contiene la documentación técnica del proyecto Android Kotlin **Carmencita Connect**. Su objetivo es facilitar el mantenimiento, la comprensión de la arquitectura y la continuidad del desarrollo por parte del equipo.

## Contenido

- [Arquitectura MVVM](arquitectura_mvvm.md): explica la separación usada entre Model/Data, ViewModel, Controller y View.
- [Módulos principales](modulos_principales.md): describe las responsabilidades de las clases más importantes del proyecto.
- [Flujos funcionales](flujos_funcionales.md): resume el comportamiento implementado para las historias de usuario principales.
- [Guía de mantenimiento](guia_mantenimiento.md): reúne recomendaciones para realizar cambios sin romper el flujo actual.

## Convención usada en el proyecto

El proyecto sigue una organización basada en capas:

```text
VIEW XML -> CONTROLLER -> VIEWMODEL -> MODEL / DATA
```

En esta implementación:

- **View**: archivos XML ubicados en `app/src/main/res/layout`.
- **Controller**: `Fragment` y `DialogFragment`, ubicados en `app/src/main/java/com/carmencita/connect/ui`.
- **ViewModel**: clases ubicadas en `app/src/main/java/com/carmencita/connect/viewmodel`.
- **Model / Data**: modelos y repositorios ubicados en `model` y `data`.

## Nota

La documentación describe el estado actual del código. Si se agregan nuevas pantallas, repositorios, modelos o historias de usuario, se recomienda actualizar estos archivos.
