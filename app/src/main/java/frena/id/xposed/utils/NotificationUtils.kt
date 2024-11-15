package frena.id.xposed.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import frena.id.service.TimerForegroundService

import android.content.ComponentName
import android.graphics.BitmapFactory


class NotificationUtils(context:Context) {

    private var mContext = context
    private lateinit var notificationBuilder: NotificationCompat.Builder
    val notificationManager = NotificationManagerCompat.from(mContext)
    val CHANNEL_ID = "f.Rina_Channel"
    val NOTIFICATION_ID = 69
    val EXTRA_CANCEL_TIMER_FROM_NOTIFICATION =
        "CANCEL_TIMER_FROM_NOTIFICATION"


    init {
        createNotificationChannel()
        initNotificationBuilderButton()
    }


    fun launchNotification(){
        with(NotificationManagerCompat.from(mContext)) {
            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    fun cancelNotification(){
        notificationBuilder.clearActions()
    }

    fun getNotification():Notification{
      return  notificationBuilder.build()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "f.Rina"
            val descriptionText = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notifiManager: NotificationManager =
                mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notifiManager.createNotificationChannel(channel)
        }
    }


    private fun initNotificationBuilderButton() {

        /***
         * Note: In Android 10 (API level 29) and higher,
         * the platform automatically generates notification action buttons if an app does not provide its own.
         * If you don't want your app's notifications to display any suggested replies or actions,
         * you can opt-out of system-generated replies and actions by using
         * setAllowGeneratedReplies() and setAllowSystemGeneratedContextualActions().
         */

        val stopIIntent = Intent(mContext, TimerForegroundService::class.java)
        stopIIntent.putExtra(EXTRA_CANCEL_TIMER_FROM_NOTIFICATION, true)

        val servicePendingIntent = PendingIntent.getService(
            mContext, 0, stopIIntent, PendingIntent.FLAG_UPDATE_CURRENT)



//        val sampleIntent = Intent(mContext, MainActivity::class.java)
//        sampleIntent.action = Intent.ACTION_MAIN
//        sampleIntent.addCategory(Intent.CATEGORY_LAUNCHER)
//        sampleIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

//        val sampleIntent = Intent(mContext, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//
//        }

//        val pendingIntent: PendingIntent = PendingIntent.getActivity(mContext, 0, sampleIntent, 0)
        
        /***
         * Notice that the NotificationCompat.Builder constructor requires that you provide a channel ID.
         * This is required for compatibility with Android 8.0 (API level 26) and higher,
         * but is ignored by older versions.
         */

    }

}