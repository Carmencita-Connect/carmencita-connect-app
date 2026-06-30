# Guía de mantenimiento

Este documento reúne recomendaciones para mantener el proyecto de forma ordenada.

## Mantener la separación por capas

Al agregar nuevas funcionalidades, se recomienda respetar la separación actual:

- Los XML deben contener la estructura visual.
- Los fragments deben capturar eventos, observar estados y navegar.
- Los ViewModel deben validar datos y coordinar llamadas.
- Los repositories deben concentrar operaciones de datos o lógica de negocio.
- Los modelos deben representar información del dominio.

## Evitar lógica de negocio en fragments

Los fragments no deberían calcular tarifas, crear pagos, generar comprobantes o construir entidades complejas. Estas tareas deben estar en ViewModel o Repository, según corresponda.

## Usar LiveData para comunicar estado

Cuando una pantalla necesite mostrar resultados, errores o cambios de estado, se recomienda exponer esos valores desde el ViewModel usando `LiveData`. El fragment debe observar esos valores y actualizar la vista.

## Reutilizar los modelos existentes

Antes de crear nuevos modelos, se debe revisar si ya existe una entidad relacionada. Por ejemplo:

- Para envíos, usar `Encomienda`.
- Para pagos, usar `Pago`.
- Para comprobantes, usar `Comprobante`.
- Para pre-registros, usar `PreRegistro`.

## Mantener los repositorios con una responsabilidad clara

Cada repositorio debe tener una responsabilidad concreta:

- `TarifaRepository`: cálculo de tarifas.
- `EncomiendaRepository`: creación o consulta de encomiendas.
- `PreRegistroRepository`: registro de pre-registros.
- `PagoRepository`: generación de pagos.
- `ComprobanteRepository`: generación de comprobantes.

## Actualizar documentación al cambiar flujos

Si se modifica una historia de usuario, se debe actualizar:

- `flujos_funcionales.md`
- `modulos_principales.md`
- Los diagramas o documentos académicos relacionados.
