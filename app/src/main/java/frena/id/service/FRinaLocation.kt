package frena.id.service

import frena.id.manager.MainActivity
import frena.id.manager.ui.map.*
import frena.id.data.repository.PreferencesRepository
import frena.id.manager.ui.map.MapScreen
import frena.id.manager.ui.map.MapViewModel
import frena.id.data.*
import frena.id.R
import frena.id.xposed.utils.PreferencesUtil
import frena.id.xposed.utils.LocationUtil

import android.util.Log
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Binder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.content.ComponentName
import android.graphics.BitmapFactory
import android.app.*
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import android.provider.Settings
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class FRinaLocation : Service(), LocationUpdatesCallBack {
    private val TAG = FRinaLocation::class.java.simpleName

    //private lateinit var locationUtil: LocationUtil
    private val mapViewModel = MapViewModel(application)
    private var notification: NotificationCompat.Builder? = null
    private var notificationManager: NotificationManager? = null

    override fun onCreate() {
        super.onCreate()
      //  locationUtil = LocationUtil()
        mapViewModel.updateClickedLocation(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_SERVICE_START -> startService()
            ACTION_SERVICE_STOP -> stopService()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    companion object {
        const val ACTION_SERVICE_START = "ACTION_START"
        const val ACTION_SERVICE_STOP = "ACTION_STOP"
    }


    private fun startService() {
        mapViewModel.updateClickedLocation(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "geoPoint",
                "GeoPoint",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        notification = NotificationCompat.Builder(this, "geoPoint")
            .setContentTitle("Tracking location...")
            .setContentText("Searching...")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)

        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        startForeground(1, notification?.build())
    }

    private fun stopService() {
        mapViewModel.updateClickedLocation(null)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

//    override fun locationException(message: String) {
//        Log.d(TAG, message)
//    }

    override fun onLocationUpdate(geoPoint: GeoPoint) {
        //PreferencesUtil.getUseRandomize() == true
       // mapViewModel.updateClickedLocation()
        val latitude = LocationUtil.latitude
        val longitude = LocationUtil.longitude
        val updatedNotification = notification?.setContentText(
            "Coordinate: ($latitude, $longitude)"
        )
        notificationManager?.notify(1, updatedNotification?.build())
    }



}



