package com.example.coronawarnpremium.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.coronawarnpremium.MainActivity
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.classes.Contact
import com.google.gson.Gson

private const val TAG = "NotificationService"
class NotificationService(private val context: Context) {

    fun requestAcceptedNotification(title: String, username: String) {

        var notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                    NotificationChannel("101", "channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, "101")
                .setContentTitle(title)
                .setContentText("$username accepted your contact request!")
                .setSmallIcon(R.drawable.ic_virus)
                .setContentIntent(getActivity(
                        context,
                        0,
                        Intent(context, MainActivity::class.java),
                        0
                ))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(1, notificationBuilder.build())

    }

    fun requestReceivedNotification(title: String, contactJson: String) {
        val gson = Gson()
        val contact = gson.fromJson(contactJson, Contact::class.java)

        val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                    NotificationChannel("101", "channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val actIntent = Intent(context, MainActivity::class.java)
        actIntent.putExtra("contact", contactJson)
        val actPending = getActivity(context, 3, actIntent, FLAG_UPDATE_CURRENT)

        /** Creating the intents and pending intents for the action buttons **/
        val acceptIntent = Intent(context, ContactRequestService::class.java)
            acceptIntent.putExtra("contact", contactJson)
            acceptIntent.putExtra("act_message", "Accepted")
        val acceptPendingIntent = getBroadcast(context, 0, acceptIntent, FLAG_UPDATE_CURRENT)


        val rejectIntent = Intent(context, ContactRequestService::class.java)
            rejectIntent.putExtra("contact", contactJson)
            rejectIntent.putExtra("act_message", "Rejected")
        val rejectPendingIntent = getBroadcast(context, 1, rejectIntent, FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(context, "101")
                .setContentTitle(title)
                .setContentText("${contact.Username} wants to add you as a contact")
                .setSmallIcon(R.drawable.ic_virus)
                .setContentIntent(actPending)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                /** Adding the action buttons **/
                .addAction(R.drawable.ic_check_circle_outline_24px, "Accept", acceptPendingIntent)
                .addAction(R.drawable.ic_lock, "Reject", rejectPendingIntent)
                .setAutoCancel(true)

        notificationManager.notify(1, notificationBuilder.build())

    }
}