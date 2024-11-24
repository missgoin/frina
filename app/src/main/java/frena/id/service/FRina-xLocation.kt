package frena.id.service

import android.app.*
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.content.ComponentName
import android.graphics.BitmapFactory
//import frena.id.service.Constants.ACTION_FOREGROUND_TIMER_BROADCAST
//import frena.id.service.Constants.EXTRA_TIMER
//import frena.id.service.Constants.timeSeconds
//import frena.id.service.GeneralCalculations
//import frena.id.service.Constants
//import frena.id.xposed.utils.NotificationUtils
import java.util.*

class ForegroundService: Service() {

    private lateinit var notificationManager: NotificationManager

    // onStartCommand can be called multiple times, so we keep track of "started" state manually
    private var isStarted = false

    override fun onCreate() {
        super.onCreate()
        // initialize dependencies here (e.g. perform dependency injection)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onDestroy() {
        super.onDestroy()
        isStarted = false
    }

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException() // bound Service is a different story
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isStarted) {
            makeForeground()
            // place here any logic that should run just once when the Service is started
            isStarted = true
        }

        // process the command here (e.g. retrieve extras from the Intent and act accordingly)
        val demoString = intent?.getStringExtra(EXTRA_DEMO) ?: ""

        return START_STICKY // makes sense for a Foreground Service, or even START_REDELIVER_INTENT
    }

    private fun makeForeground() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // before calling startForeground, we must create a notification and a corresponding
        // notification channel
        
        createServiceNotificationChannel()

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("FRina x-Location")
            .setContentText("Foreground service x-Location")
            .setSmallIcon(R.drawable.ic_your_app_logo)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(ONGOING_NOTIFICATION_ID, notification)
    }

    private fun createServiceNotificationChannel() {
        if (Build.VERSION.SDK_INT < 26) {
            return // notification channels were added in API 26
        }

        val channel = NotificationChannel(
            CHANNEL_ID,
            "FRina Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val ONGOING_NOTIFICATION_ID = 101
        private const val CHANNEL_ID = "1001"

        private const val EXTRA_DEMO = "EXTRA_DEMO"

        fun startService(context: Context, demoString: String) {
            val intent = Intent(context, ForegroundService::class.java)
            intent.putExtra(EXTRA_DEMO, demoString)
            if (Build.VERSION.SDK_INT < 26) {
                context.startService(intent)
            } else {
                context.startForegroundService(intent)
            }
        }

        fun stopService(context: Context) {
            val intent = Intent(context, ForegroundService::class.java)
            context.stopService(intent)
        }

    }
}