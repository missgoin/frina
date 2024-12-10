package frena.id.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat

import frena.id.R


/**
 * Helper class for creating and update Android notifications.
 */
class NotificationUtils(private val context: Context, private val contentIntent: PendingIntent) {

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_CHANNEL_ID = "foreground_channel_1"

        fun getNotificationId(): Int {
            return NOTIFICATION_ID
        }
    }

    fun setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "FRina Foreground",                
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                lightColor = Color.YELLOW
                enableLights(true)
                enableVibration(false)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun createNotification(icon: Int, title: Int, message: Int): Notification {
        return createNotification(icon, context.getText(title), context.getText(message))
    }

    fun updateNotification(icon: Int, title: Int, message: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager?.notify(NOTIFICATION_ID, createNotification(icon, title, message))
    }

    fun createNotification(icon: Int, title: CharSequence, message: CharSequence): Notification {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(contentIntent)
            .setOnlyAlertOnce(true)
            .build()
    }
}

