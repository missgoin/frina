package frena.id.service

import frena.id.manager.MainActivity
import frena.id.manager.ui.map.*
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

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import frena.id.data.model.FavoriteLocation
import frena.id.data.repository.PreferencesRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint


class Rina (mapviewModel: MapViewModel) : Service() {

 //   private lateinit var locationManager: LocationManager
 //   private val geocoder by lazy { Geocoder(this, Locale.getDefault()) }
 //   private var job: Job? = null
    
    private val preferencesRepository = PreferencesRepository(application)
    val isPlaying = mutableStateOf(false)
    val lastClickedLocation = mutableStateOf<GeoPoint?>(null)
    val userLocation = mutableStateOf<GeoPoint?>(null)


    override fun onCreate() {
        super.onCreate()
    //    locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> startForegroundServiceWithNotification()
            Actions.STOP.toString() -> stopSelf()
        }
        return START_STICKY
    }

    private fun startForegroundServiceWithNotification() {
        val notification = buildNotification("Starting location tracking...")
        startForeground(NOTIFICATION_ID, notification)
        updateClickedLocation()
    }

    private fun buildNotification(content: String) =
        NotificationCompat.Builder(this, LocationTrackerApp.CHANNEL_ID)
            .setContentTitle("Location Tracker")
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()


    fun updateClickedLocation(geoPoint: GeoPoint?) {
        lastClickedLocation.value = geoPoint
        val geoPoint = geoPoint
        geoPoint?.let {
            preferencesRepository.saveLastClickedLocation(
                it.latitude,
                it.longitude
            )
        } ?: preferencesRepository.clearLastClickedLocation()    
    }




    override fun onDestroy() {
        super.onDestroy()
    //    locationManager.removeUpdates { }
        stopSelf()
        
    }

    companion object {
        const val NOTIFICATION_ID = 1
    }

    enum class Actions {
        START, STOP
    }
}




