package frena.id.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import android.provider.Settings
import android.widget.Toast

import frena.id.manager.MainActivity
import frena.id.manager.ui.map.*
import frena.id.data.repository.PreferencesRepository
import frena.id.manager.ui.map.MapScreen
import frena.id.manager.ui.map.MapViewModel
import frena.id.data.*
import frena.id.data.DEFAULT_FRINA_SERVICE
import frena.id.data.name
import frena.id.data.key
import frena.id.xposed.hooks.GojekApiHooks
import frena.id.xposed.utils.PreferencesUtil
import frena.id.xposed.utils.GojekUtil
import android.util.Log
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Binder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.content.ComponentName



class FRinaxService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
    }


// This is triggered when another android component sends an Intent to this running service
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

// Do the work that the service needs to do here
        When (intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    enum class Actions {
        START, STOP
    }

    private fun start() {

        val notification = NotificationCompat.Builder(this, "ForegroundServiceChannelId")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Foreground Service")
            .setContentText("Foreground service is running")
            .build()

        // Start the service in the foreground
        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up any resources here
    }
    
}