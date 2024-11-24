package frena.id.service

import android.app.*
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import frena.id.service.Constants.ACTION_FOREGROUND_TIMER_BROADCAST
import frena.id.service.Constants.EXTRA_TIMER
import frena.id.service.Constants.timeSeconds
import frena.id.service.GeneralCalculations
import frena.id.service.Constants
import frena.id.xposed.utils.NotificationUtils
import java.util.*

class TimerForegroundService : Service() {

    private var isServiceRunningInForeground = false
    private val localBinder = LocalBinder()
    private lateinit var notificationUtils: NotificationUtils

    private lateinit var timer: Timer

    private var  mIntent:Intent =Intent(ACTION_FOREGROUND_TIMER_BROADCAST)

    override fun onCreate() {
        super.onCreate()

        notificationUtils = NotificationUtils(this)


        mIntent.putExtra(EXTRA_TIMER, GeneralCalculations.getStopWatchTimer(timeSeconds))
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(mIntent)

        // Updates notification content if this service is running as a foreground
        // service.
        if (isServiceRunningInForeground) {
            notificationUtils.launchNotification()
        }

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val cancelLocationTrackingFromNotification =
            intent.getBooleanExtra(
                notificationUtils.EXTRA_CANCEL_TIMER_FROM_NOTIFICATION,
                false
            )

        if (cancelLocationTrackingFromNotification) {
            onSWatchStop()
            stopSelf()
        }
        // Tells the system not to recreate the service after it's been killed.
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder {
        stopForeground(true)
        isServiceRunningInForeground = false
        return localBinder
    }

    override fun onRebind(intent: Intent) {

        stopForeground(true)
        isServiceRunningInForeground = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {

        startForeground(notificationUtils.NOTIFICATION_ID, notificationUtils.getNotification())
        isServiceRunningInForeground = true

        // Ensures onRebind() is called if MainActivity (client) rebinds.
        return true
    }

    fun onSWatchStart() {

        startService(Intent(applicationContext, TimerForegroundService::class.java))

        timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                timeSeconds += 1
                mIntent.putExtra(EXTRA_TIMER, GeneralCalculations.getStopWatchTimer(timeSeconds))
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(mIntent)
            }
        }, 0, 1000)


    }

    fun onSWatchStop() {
        try {
            timer.cancel()
        }catch (e:Exception){

        }

        stopSelf()
    }

    fun destroyService(){
        onSWatchStop()
        stopSelf()
    }


    inner class LocalBinder : Binder() {
        internal val service: TimerForegroundService
            get() = this@TimerForegroundService
    }

}