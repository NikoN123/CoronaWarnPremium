package com.example.coronawarnpremium.signalr

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.classes.Contact
import com.example.coronawarnpremium.services.NotificationService
import com.example.coronawarnpremium.storage.contact.ContactDatabaseClient
import com.example.coronawarnpremium.storage.user.UserDatabaseClient
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.microsoft.signalr.HubConnectionBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private const val TAG = "SignalRClient"
class SignalRClient (private val context: Context) : CoroutineScope by MainScope() {
    private var hub = HubConnectionBuilder.create("http://localhost:5000/api/1/contact/connect")
    //private var hub = HubConnectionBuilder.create("http://10.0.2.2:5000/api/1/contact/connect")

            .build()
    private val gson = Gson()

    fun createConnection(){
        Log.v(TAG, "Creating connection!")
        try {
            launch(Dispatchers.Main) {
                establishHubMethods()
            }
        } catch(e: Exception){
            Log.e(TAG, "Error: ${e.message}")
        }
    }

    suspend fun establishHubMethods(){

        /** Method 1: **/
        hub.on(
            "RequestConnection",
            { message: Contact ->
                Log.e(TAG, "Id: $message")
            },
            Contact::class.java
        )

        hub.on("ContactRequestAccepted",
                { _:String, contact: Contact ->
                    Log.v(TAG, "Request accepted by: $contact")
                    launch(Dispatchers.Main) {
                        if(UserDatabaseClient(context).getUser(contact.id) == null) {
                            ContactDatabaseClient(context).insert(contact)
                            NotificationService(context).requestAcceptedNotification(contact.username)
                        }
                    }
        }, String::class.java, Contact::class.java)

        hub.on("ContactRequestDeclined",
                { _, email ->
                    launch(Dispatchers.Main) {
                        val user = UserDatabaseClient(context).getAllUsers()
                        if(user?.eMail != email) {
                            Toast.makeText(context, "Your contact request was denied by $email.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }, String::class.java, String::class.java)



        hub.on(
                "RequestContact",
                { id: String, contact: Contact ->
                    launch(Dispatchers.Main) {
                        if (UserDatabaseClient(context).getUser(contact.id) == null) {
                            val json = gson.toJson(contact)
                            val not = NotificationService(context)
                            not.requestReceivedNotification(json, id)
                        }
                    }
                },
                String::class.java, Contact::class.java
        )


        hub.start()
    }

    fun checkConnectionStatus(): String{
        Log.v(TAG, "Connection state: ${hub.connectionState}")
        return hub.connectionState.toString()
    }
    private fun connectionClosed(){
        Log.v(TAG, "Connection was closed")
    }
}