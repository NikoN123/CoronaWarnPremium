package com.example.coronawarnpremium.services

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.coronawarnpremium.classes.Contact
import com.example.coronawarnpremium.storage.contact.ContactDatabaseClient
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class ContactRequestService : BroadcastReceiver(), CoroutineScope by MainScope(){
    private val TAG = "ContactRequestService"
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(intent.getStringExtra("act_message") == "Accepted") {
            Log.v(TAG, intent.getStringExtra("act_message")!!)
            Log.v(TAG, intent.getStringExtra("contact")!!)
            notificationManager.cancel(1)
            //send data to backend

            //saving contact
            val gson = Gson()
            launch(Dispatchers.Main) {
                ContactDatabaseClient(context).insert(gson.fromJson(intent.getStringExtra("contact"), Contact::class.java))
            }
        }
        else if(intent.getStringExtra("act_message") == "Rejected"){
            notificationManager.cancel(1)
            //send rejection to backend
            Log.v(TAG, intent.getStringExtra("act_message")!!)
        }
        else{
            Toast.makeText(context, "Action message ${intent.getStringExtra("act_message")} is not known.", Toast.LENGTH_SHORT).show()
        }
    }
}