package com.carmencita.connect.ui.alertas

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.carmencita.connect.R
import com.carmencita.connect.model.AlertaCambioEstado

class AlertaLocalNotifier(private val context: Context) {

    fun mostrar(cambio: AlertaCambioEstado) {
        if (!puedeNotificar()) return

        crearCanalSiCorresponde()
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_history)
            .setContentTitle("Cambio de estado")
            .setContentText(cambio.mensaje)
            .setStyle(NotificationCompat.BigTextStyle().bigText(cambio.mensaje))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    private fun puedeNotificar(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
    }

    private fun crearCanalSiCorresponde() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Alertas de estado",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private companion object {
        const val CHANNEL_ID = "alertas_estado"
        const val NOTIFICATION_ID = 9001
    }
}
