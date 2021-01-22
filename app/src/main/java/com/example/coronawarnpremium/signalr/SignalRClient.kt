package com.example.coronawarnpremium.signalr

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.coronawarnpremium.classes.Contact
import com.example.coronawarnpremium.services.NotificationService
import com.google.gson.Gson
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "SignalRClient"
class SignalRClient (private val context: Context) : CoroutineScope by MainScope() {
    private var hub = HubConnectionBuilder.create("http://our-fancy-url.com").build()
    private val gson = Gson()

    fun createConnection(){
        Log.v(TAG, "Creating connection!")
        try {
            launch(Dispatchers.Main) {
                establishHubMethods()
            }
        } catch(e: Exception){
            Log.v(TAG, "Error: ${e.message}")
        }
    }

    suspend fun establishHubMethods(){

        /** Method 1: **/
        hub.on(
            "infectedId",
            { message: String ->
                println("New Message: $message")
            },
            String::class.java
        )

        hub.on("requestAccepted",
                { contact: Contact ->
            Log.v(TAG, "Found contact: ${contact.Username}")
        }, Contact::class.java)

        hub.on("requestDenied",
                { contact: Contact ->
                    Log.v(TAG, "Found contact: ${contact.Username}")
                }, Contact::class.java)

        hub.on("contactRequest", {
            contact: Contact ->
            val json = gson.toJson(contact)
            val not = NotificationService(context)
            not.requestReceivedNotification("Request accepted", json)
        }, Contact::class.java)

        hub.start()
    }

    fun checkConnectionStatus(): String{
        return hub.connectionState.toString()
    }
}