package frena.id.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class buttonrecevier:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action=="BUUTTENACCTION")
        {
            val msg=intent.extras?.getString("msg")?:"not found"
            Log.e("msg","$msg")
        }
    }
}

