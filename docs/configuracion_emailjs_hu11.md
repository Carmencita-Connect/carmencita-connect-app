# Configuración de EmailJS para HU11

## 1. Crear el servicio

1. Ingresar a `https://dashboard.emailjs.com/`.
2. Abrir **Email Services**.
3. Seleccionar **Add New Service** y conectar Gmail u Outlook.
4. Copiar el valor **Service ID**.

## 2. Crear la plantilla

1. Abrir **Email Templates**.
2. Crear una plantilla nueva.
3. Configurar:
   - **To Email:** `{{to_email}}`
   - **Subject:** `Tu código de confirmación | Carmencita Express`
4. Copiar en el contenido HTML la plantilla:
   - `docs/emailjs_template_hu11.html`
5. Guardar y copiar el **Template ID**.

La app envía estas variables:

- `{{to_email}}`
- `{{nombre}}`
- `{{codigo}}`
- `{{name}}`
- `{{email}}`
- `{{title}}`
- `{{message}}`
- `{{marca}}`
- `{{color_principal}}`
- `{{color_secundario}}`

## 3. Obtener la Public Key

1. Abrir **Account**.
2. Entrar a **General**.
3. Copiar la **Public Key**.

## 4. Habilitar solicitudes desde Android

1. Abrir **Account**.
2. Entrar a **Security**.
3. Habilitar **Allow EmailJS API for non-browser applications**.
4. Deshabilitar **Strict Mode** para que la API móvil funcione únicamente con la Public Key.

Android realiza la solicitud mediante la API REST y EmailJS la identifica como una
aplicación no navegador. Si Strict Mode está activo, EmailJS exige la Private Key.
La Private Key no debe incluirse dentro de una aplicación Android porque puede
extraerse del APK.

## 5. Configurar el proyecto

Agregar los valores sin comillas en `local.properties`:

```properties
EMAILJS_SERVICE_ID=service_xxxxx
EMAILJS_TEMPLATE_ID=template_xxxxx
EMAILJS_PUBLIC_KEY=xxxxxxxxxxxx
```

Después ejecutar **Sync Project with Gradle Files** y volver a compilar la app.

`local.properties` está excluido de Git, por lo que estos valores no se subirán al repositorio.
