# Guia de mantenimiento

Este documento reune recomendaciones para mantener el proyecto de forma ordenada.

## Mantener la separacion por capas

Al agregar nuevas funcionalidades, se recomienda respetar la separacion actual:

- Los XML deben contener la estructura visual.
- Los fragments deben capturar eventos, observar estados y navegar.
- Los ViewModel deben validar datos y coordinar llamadas.
- Los repositories deben concentrar operaciones de datos o logica de negocio.
- Los modelos deben representar informacion del dominio.

## Evitar logica de negocio en fragments

Los fragments no deberian calcular tarifas, crear pagos, generar comprobantes o construir entidades complejas. Estas tareas deben estar en ViewModel o Repository, segun corresponda.

## Usar LiveData para comunicar estado

Cuando una pantalla necesite mostrar resultados, errores o cambios de estado, se recomienda exponer esos valores desde el ViewModel usando `LiveData`. El fragment debe observar esos valores y actualizar la vista.

## Reutilizar los modelos existentes

Antes de crear nuevos modelos, se debe revisar si ya existe una entidad relacionada. Por ejemplo:

- Para envios, usar `Encomienda`.
- Para pagos, usar `Pago`.
- Para comprobantes, usar `Comprobante`.
- Para pre-registros, usar `PreRegistro`.

## Mantener los repositorios con una responsabilidad clara

Cada repositorio debe tener una responsabilidad concreta:

- `TarifaRepository`: calculo de tarifas.
- `EncomiendaRepository`: creacion o consulta de encomiendas.
- `PreRegistroRepository`: registro de pre-registros.
- `PagoRepository`: generacion de pagos.
- `ComprobanteRepository`: generacion de comprobantes.
- `SedeRepository`: consulta de sedes.
- `NotificacionRepository`: registro y consulta de notificaciones.
- `AlertaEstadoRepository`: configuracion y simulacion de cambios de estado.

## Mantener la documentacion organizada

La carpeta `documentacion` esta separada por tema:

- `documentacion/arquitectura`: arquitectura, modulos, flujos y mantenimiento.
- `documentacion/configuracion`: servicios externos y configuraciones.
- `documentacion/funcionalidades`: documentacion funcional especifica.
- `documentacion/manuales`: manuales de usuario y entrega.
- `documentacion/pruebas`: evidencias, checklist y pruebas unitarias.

## Actualizar documentacion al cambiar flujos

Si se modifica una historia de usuario, se debe actualizar:

- `documentacion/arquitectura/flujos_funcionales.md`
- `documentacion/arquitectura/modulos_principales.md`
- Los diagramas o documentos academicos relacionados.
- Los checklist o documentos de prueba dentro de `documentacion/pruebas`, si aplica.
