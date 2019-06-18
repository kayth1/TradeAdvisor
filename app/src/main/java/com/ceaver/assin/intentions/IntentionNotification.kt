package com.ceaver.assin.intentions

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import com.ceaver.assin.MyApplication
import com.ceaver.assin.R
import com.ceaver.assin.StartActivity
import com.ceaver.assin.extensions.format
import com.ceaver.assin.extensions.resIdByName
import java.util.*

object IntentionNotification {

    const val CHANNEL_ID = "intention"

    init {
        // Create the NotificationChannel, but only on API 26+ because the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Intention Channel"
            val description = "Notification if an intention changes state"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance or other notification behaviors after this
            ContextCompat.getSystemService(MyApplication.appContext!!, NotificationManager::class.java)!!.createNotificationChannel(channel)
        }
    }

    fun notify(intention: Intention) {

        val intent = Intent(MyApplication.appContext!!, StartActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) // TODO back button must end in Markets
        val pendingIntent = PendingIntent.getActivity(MyApplication.appContext!!, 0, intent, 0)

        val notification = NotificationCompat.Builder(MyApplication.appContext!!, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(BitmapFactory.decodeResource(MyApplication.appContext!!.resources, getImageIdentifier(intention.title.symbol)))
                .setContentTitle("${intention.title.symbol} Intention Change")
                .setContentText("New State: ${intention.status} (${intention.percentToReferencePrice().format("abc")}%)")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

        val notificationManager = NotificationManagerCompat.from(MyApplication.appContext!!)
        // notificationId is a unique int for each notification that you must define
        // Remember to save the notification ID that you pass to NotificationManagerCompat.notify() because you'll need it later if you want to update or remove the notification.
        notificationManager.notify(Random().nextInt(), notification);
    }

    private fun getImageIdentifier(symbol: String): Int {
        val identifier = MyApplication.appContext!!.resIdByName(symbol.toLowerCase(), "drawable")
        return if (identifier == 0) R.drawable.unknown else identifier
    }
}