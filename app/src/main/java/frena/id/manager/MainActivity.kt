package frena.id.manager

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.content.Context
import android.content.Intent
import android.os.Build

import frena.id.service.FRinaLocation
import frena.id.data.repository.PreferencesRepository
import frena.id.data.name
import frena.id.data.key

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

class MainActivity : ComponentActivity() {
    private val tag = "MainActivity"
    
    private val preferencesRepository = PreferencesRepository(application)


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
    
    
    enum class Actions {
        START,
        STOP
    }

    fun actionOnService(action: Actions) {
        if (preferencesRepository.getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return
        Intent(this, FRinaLocation::class.java).also {
            it.action = action.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //    Log.d("Starting the service in >=26 Mode")
                startForegroundService(it)
                return
            }
        //    Log.d("Starting the service in < 26 Mode")
            startService(it)
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



