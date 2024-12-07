package frena.id.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import frena.id.data.*
import frena.id.data.DEFAULT_FRINA_SERVICE
import frena.id.data.name
import frena.id.data.key
import frena.id.xposed.utils.PreferencesUtil
import frena.id.data.repository.PreferencesRepository


class StartReceiver : BroadcastReceiver() {

    enum class Actions {
        START,
        STOP
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED && getServiceState(context) == ServiceState.STARTED) {
            Intent(context, FRinaService::class.java).also {
                it.action = Actions.START.name
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    log("Starting the service in >=26 Mode from a BroadcastReceiver")
                    context.startForegroundService(it)
                    return
                }
                log("Starting the service in < 26 Mode from a BroadcastReceiver")
                context.startService(it)
            }
        }
    }
}