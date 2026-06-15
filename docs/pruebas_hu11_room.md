# Pruebas HU11 - Registro y perfil con Room

## Pruebas unitarias automatizadas

Estas pruebas cubren la lógica que no depende de Android UI:

- Login con campos vacíos: debe retornar error.
- Login con correo inválido: debe retornar error.
- Login con correo y contraseña válidos: no debe retornar error.
- Registro con campos vacíos: debe retornar error.
- Registro con DNI inválido: debe retornar error.
- Registro con teléfono inválido: debe retornar error.
- Registro con contraseña débil: debe retornar error.
- Registro con contraseñas diferentes: debe retornar error.
- Registro con datos válidos: no debe retornar error.
- Hash de contraseña correcta: debe validar correctamente.
- Hash con contraseña incorrecta: debe rechazar el acceso.
- Hash con salts distintos: debe generar hashes distintos.

## Pruebas manuales necesarias

- Registrar un usuario nuevo y verificar que ingrese al inicio autenticado.
- Intentar registrar el mismo correo y verificar el mensaje de correo registrado.
- Intentar registrar el mismo DNI y verificar el mensaje de DNI registrado.
- Cerrar sesión y verificar que vuelva al login.
- Después de cerrar sesión, entrar como invitado y verificar que no aparezca el nombre del usuario anterior.
- Entrar como invitado y verificar que no aparezcan los botones de Mi Perfil ni Cerrar sesión.
- Iniciar sesión con el usuario registrado y verificar que aparezca el saludo con su nombre.
- Abrir Mi Perfil y verificar nombre, DNI y teléfono.
- Editar teléfono y verificar que el cambio persista al volver a Mi Perfil.
- Abrir PreRegistro con sesión activa y presionar Usar mis datos para autocompletar remitente.
