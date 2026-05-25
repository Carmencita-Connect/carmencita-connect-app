package com.carmencita.connect.data

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.carmencita.connect.model.Comprobante
import com.carmencita.connect.model.Pago
import com.carmencita.connect.model.PreRegistro
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.kernel.geom.PageSize
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ComprobanteRepository {

    fun generarPDF(
        context: Context,
        pago: Pago,
        preRegistro: PreRegistro
    ): Boolean {
        return try {
            val comprobante = Comprobante(
                id             = (1000..9999).random(),
                pago           = pago,
                total          = pago.monto,
                fechaGeneracion = SimpleDateFormat(
                    "dd/MM/yyyy HH:mm", Locale.getDefault()
                ).format(Date())
            )

            val nombreArchivo = "Boleta_${pago.numeroPR}_${System.currentTimeMillis()}.pdf"
            val outputStream = obtenerOutputStream(context, nombreArchivo) ?: return false

            val writer = PdfWriter(outputStream)
            val pdfDoc = PdfDocument(writer)
            val document = Document(pdfDoc, PageSize.A4)
            document.setMargins(40f, 40f, 40f, 40f)

            document.add(
                Paragraph("CARMENCITA EXPRESS CARGO S.A.C.")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(16f).setBold()
            )
            document.add(
                Paragraph("BOLETA DE VENTA ELECTRÓNICA")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(13f)
            )
            document.add(Paragraph(" "))

            document.add(Paragraph("N° Pre-registro: ${pago.numeroPR}").setFontSize(12f).setBold())
            document.add(Paragraph("Fecha: ${comprobante.fechaGeneracion}").setFontSize(11f))
            document.add(Paragraph(" "))

            document.add(Paragraph("DATOS DEL REMITENTE").setFontSize(12f).setBold())
            document.add(Paragraph("Nombre: ${preRegistro.remitente.nombre}").setFontSize(11f))
            document.add(Paragraph("DNI: ${preRegistro.remitente.dni}").setFontSize(11f))
            document.add(Paragraph("Teléfono: ${preRegistro.remitente.telefono}").setFontSize(11f))
            document.add(Paragraph("Dirección: ${preRegistro.remitente.direccion}").setFontSize(11f))
            document.add(Paragraph(" "))

            document.add(Paragraph("DATOS DEL DESTINATARIO").setFontSize(12f).setBold())
            document.add(Paragraph("Nombre: ${preRegistro.destinatario.nombre}").setFontSize(11f))
            document.add(Paragraph("DNI: ${preRegistro.destinatario.dni}").setFontSize(11f))
            document.add(Paragraph("Teléfono: ${preRegistro.destinatario.telefono}").setFontSize(11f))
            document.add(Paragraph("Dirección: ${preRegistro.destinatario.direccion}").setFontSize(11f))
            document.add(Paragraph(" "))

            document.add(Paragraph("DATOS DEL ENVÍO").setFontSize(12f).setBold())
            document.add(Paragraph("Descripción: ${preRegistro.descripcionCarga}").setFontSize(11f))
            document.add(Paragraph("Origen: ${preRegistro.encomienda.origen}").setFontSize(11f))
            document.add(Paragraph("Destino: ${preRegistro.encomienda.destino}").setFontSize(11f))
            document.add(Paragraph("Peso: ${preRegistro.encomienda.peso}kg").setFontSize(11f))
            document.add(Paragraph("Método de pago: ${pago.metodo}").setFontSize(11f))
            document.add(Paragraph(" "))

            document.add(
                Paragraph("TOTAL A PAGAR: S/ ${"%.2f".format(comprobante.total)}")
                    .setFontSize(14f).setBold()
                    .setTextAlignment(TextAlignment.RIGHT)
            )
            document.add(Paragraph(" "))

            document.add(
                Paragraph("Gracias por confiar en Carmencita Express Cargo")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(10f)
            )

            document.close()
            true

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun obtenerOutputStream(
        context: Context,
        nombreArchivo: String
    ): OutputStream? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, nombreArchivo)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val uri = context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                contentValues
            )
            uri?.let { context.contentResolver.openOutputStream(it) }
        } else {
            val dir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            )
            dir.mkdirs()
            val file = java.io.File(dir, nombreArchivo)
            java.io.FileOutputStream(file)
        }
    }
}