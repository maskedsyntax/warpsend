package dev.warpsend.data.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.warpsend.domain.TransferEngine
import javax.inject.Inject

@AndroidEntryPoint
class TransferService : Service() {

    @Inject lateinit var transferEngine: TransferEngine

    private val notificationId = 101
    private val channelId = "transfer_channel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification("Starting transfer...")
        startForeground(notificationId, notification)
        
        // TODO: Handle intent to start specific transfer
        
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(content: String): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("WarpSend Transfer")
            .setContentText(content)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "File Transfers",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
