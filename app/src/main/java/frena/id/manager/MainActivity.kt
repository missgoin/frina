package frena.id.manager

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.content.Context
import android.content.Intent
import android.os.Build

import frena.id.data.repository.PreferencesRepository

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import frena.id.manager.ui.navigation.AppNavGraph
import frena.id.manager.ui.theme.frenaTheme
import org.osmdroid.config.Configuration
import kotlin.system.exitProcess

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Binder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.graphics.Color
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import android.provider.Settings
import android.widget.Toast

//import frena.id.service.FRinaxService

class MainActivity : ComponentActivity() {
    private val tag = "MainActivity"
    
    @SuppressLint("WorldReadableFiles")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var isXposedModuleEnabled = true
        
        // If the module is not enabled then the app won't have permission to use MODE_WORLD_READABLE.
        try {
            Configuration.getInstance().load(this, getPreferences(MODE_WORLD_READABLE))
        } catch (e: SecurityException) {
            isXposedModuleEnabled = false
            Log.e(tag, "SecurityException: ${e.message}")
        } catch (e: Exception) {
            isXposedModuleEnabled = false
            Log.e(tag, "Exception: ${e.message}")
        }

        if (isXposedModuleEnabled) {
            enableEdgeToEdge()
            setContent {
                frenaTheme {
                    val navController = rememberNavController()
                    AppNavGraph(navController = navController)
                }
            }
        } else {
            setContent {
                frenaTheme {
                    ErrorScreen()
                }
            }
        }
        

        
    }
    


}

@Composable
fun ErrorScreen() {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Module Not Active") },
        text = {
            Text( "FRina module is not active in your Xposed manager app. Please enable it and restart the app to continue." )
        },
        confirmButton = {
            Button(onClick = {
                exitProcess(0)
            }) {
                Text("OK")
            }
        },
        dismissButton = null
    )
}



