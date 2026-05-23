package com.carmencita.connect.data

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.LineSeparator
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.layout.borders.SolidBorder
import java.io.OutputStream

class ComprobanteRepository {

    fun generarPDF(
        context: Context,
        numeroPR: String,
        remitente: String,
        destinatario: String,
        origen: String,
        destino: String,
        costo: Double,
        peso: Double,
        metodoPago: String
    ): Boolean {
        return try {
            val nombreArchivo = "Boleta_${numeroPR}_${System.currentTimeMillis()}.pdf"
            val outputStream = obtenerOutputStream(context, nombreArchivo)
                ?: return false

            val writer = PdfWriter(outputStream)
            val pdfDoc = PdfDocument(writer)
            val document = Document(pdfDoc, PageSize.A4)
            document.setMargins(40f, 40f, 40f, 40f)

            // Título
            document.add(
                Paragraph("CARMENCITA EXPRESS CARGO S.A.C.")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(16f)
                    .setBold()
            )

            document.add(
                Paragraph("BOLETA DE VENTA ELECTRÓNICA")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(13f)
            )

            document.add(Paragraph(" "))

            // Número de pre-registro
            document.add(
                Paragraph("N° Pre-registro: $numeroPR")
                    .setFontSize(12f)
                    .setBold()
            )

            document.add(
                Paragraph("Fecha: ${java.text.SimpleDateFormat(
                    "dd/MM/yyyy HH:mm",
                    java.util.Locale.getDefault()
                ).format(java.util.Date())}")
                    .setFontSize(11f)
            )

            document.add(Paragraph(" "))

            // Datos del envío
            document.add(
                Paragraph("DATOS DEL ENVÍO")
                    .setFontSize(12f)
                    .setBold()
            )

            document.add(Paragraph("Remitente: $remitente").setFontSize(11f))
            document.add(Paragraph("Destinatario: $destinatario").setFontSize(11f))
            document.add(Paragraph("Origen: $origen").setFontSize(11f))
            document.add(Paragraph("Destino: $destino").setFontSize(11f))
            document.add(Paragraph("Peso: ${peso}kg").setFontSize(11f))
            document.add(Paragraph("Método de pago: $metodoPago").setFontSize(11f))

            document.add(Paragraph(" "))

            // Total
            document.add(
                Paragraph("TOTAL A PAGAR: S/ ${"%.2f".format(costo)}")
                    .setFontSize(14f)
                    .setBold()
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
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DOWNLOADS
                )
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