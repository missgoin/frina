package frena.id.service

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import frena.id.data.*
import frena.id.data.key
import frena.id.data.DEFAULT_FRINA_SERVICE
import frena.id.data.name

class ServiceTracker (context: Context) {

    private val tag = "FRina Service"
    
    @SuppressLint("WorldReadableFiles")
    private val sharedPrefs = try {
        context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_WORLD_READABLE)
    } catch (e: SecurityException) {
        context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE) // Fallback to MODE_PRIVATE
    }
    

enum class ServiceState {
    STARTED, STOPPED;

    companion object {
        fun toServiceState(myEnumString: String): ServiceState {
            return try {
                valueOf(myEnumString)
            } catch (ex: Exception) {
                // For error cases
                STOPPED
            }
        }
    }
}

    fun setServiceState(context: Context, myEnum: ServiceState) {
        sharedPrefs.edit()
        .putString(key, myEnum.toString())
        .apply()
    }

    fun getServiceState(context: Context): ServiceState {
        val myEnumString: String? = sharedPrefs.getString(key, ServiceState.STOPPED.toString())
        return ServiceState.toServiceState(myEnumString)
    }


}


