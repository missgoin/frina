package frena.id.service

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.NonNull

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import frena.id.manager.MainActivity
import frena.id.manager.ui.map.*
import frena.id.data.repository.PreferencesRepository
import frena.id.manager.ui.map.MapScreen
import frena.id.manager.ui.map.MapViewModel
import frena.id.R

import java.util.List


interface BackgroundServiceListener {
    fun onStateUpdate(state: HEREBackgroundPositioningService.State)
}

class HEREBackgroundPositioningService : Service() {
    enum class State { STOPPED, RUNNING }

    private val TAG = HEREBackgroundPositioningService::class.java.simpleName
    private val KEY_CONTENT_INTENT = "contentIntent"
    private var running: Boolean = false
    private lateinit var notificationUtils: NotificationUtils
    private var serviceListener: BackgroundServiceListener? = null
    private var serviceState: State = State.STOPPED


    /**
     * Class for clients to access. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    inner class LocalBinder : Binder() {
        fun getService(): HEREBackgroundPositioningService {
            return this@HEREBackgroundPositioningService
        }
    }


// Start foreground service.
fun start(context: Context) {
    if (running) {
        return
    }
    val activityIntent = Intent(context, MapViewModel::class.java)
    val contentIntent = createPendingIntentGetActivity(context, 0, activityIntent, 0)
    val serviceIntent = Intent(context, HEREBackgroundPositioningService::class.java).apply {
        putExtra(KEY_CONTENT_INTENT, contentIntent)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(serviceIntent)
    } else {
        context.startService(serviceIntent)
    }
}

// Stop foreground service.
fun stop(context: Context) {
    val serviceIntent = Intent(context, HEREBackgroundPositioningService::class.java)
    context.stopService(serviceIntent)
}

@Suppress("deprecation")
override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    // The service is starting, due to a call to startService()
    running = true
    val contentIntent = intent?.getParcelableExtra<PendingIntent>(KEY_CONTENT_INTENT)
    notificationUtils = NotificationUtils(applicationContext, contentIntent)
    return startForegroundService()
}

override fun onDestroy() {
    super.onDestroy()
    // The service is no longer used and is being destroyed
    stopLocating()
    running = false
}

override fun onBind(intent: Intent?): IBinder {
    return binder
}

// This is the object that receives interactions from clients.
private val binder = LocalBinder()

// Start foreground service.
// Returns start flags, which will be passed to OS
private fun startForegroundService(): Int {
    notificationUtils.setupNotificationChannel()
    setStateValue(State.STARTING)
    startForeground(
        NotificationUtils.getNotificationId(),
        notificationUtils.createNotification(
            R.drawable.update_notification_status_bar,
            R.string.status_yellow_title,
            R.string.status_yellow
        )
    )
    handleConsent()
    if (!startLocating()) {
        setStateStopped()
        stopSelf()
    }
    return START_NOT_STICKY
}



// Sets service state to STOPPED and updates notification.
private fun setStateStopped() {
    if (!setStateValue(State.STOPPED)) {
        return
    }
    notificationUtils.updateNotification(
        R.drawable.update_notification_status_bar,
        R.string.status_grey_title,
        R.string.status_grey
    )
}


// Sets service state to RUNNING and updates notification.
private fun setStateRunning() {
    if (!setStateValue(State.RUNNING)) {
        return
    }
    notificationUtils.updateNotification(
        R.drawable.update_notification_status_bar,
        R.string.status_green_title,
        R.string.status_green
    )
}

// Set service state and report to registered listener if the state changes.
private fun setStateValue(state: State): Boolean {
    if (serviceState == state) {
        return false
    }
    serviceState = state
    reportStateValue()
    return true
}

// Reports service state to registered listener.
private fun reportStateValue() {
    val listener = serviceListener
    listener?.onStateUpdate(serviceState)
}

private fun createPendingIntentGetActivity(context: Context, id: Int, intent: Intent, flag: Int): PendingIntent? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_IMMUTABLE or flag)
    } else {
        PendingIntent.getActivity(context, id, intent, flag)
    }
}




}







