package com.example.coronawarnpremium

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.coronawarnpremium.classes.Contact
import com.example.coronawarnpremium.classes.InfectedUsers
import com.example.coronawarnpremium.classes.User
import com.example.coronawarnpremium.services.ApiService
import com.example.coronawarnpremium.services.ConnectionsApiService
import com.example.coronawarnpremium.services.NotificationService
import com.example.coronawarnpremium.signalr.SignalRClient
import com.example.coronawarnpremium.storage.contact.ContactDatabaseClient
import com.example.coronawarnpremium.storage.infected.InfectedDatabaseClient
import com.example.coronawarnpremium.storage.user.UserDatabaseClient
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_account, R.id.nav_contact_book, R.id.nav_contact_diary), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val myContext: Context = this

        var user = User(id = "", username = "", eMail = "", created = "", infected = false, token = "", location = "", name = "")
        val gson = Gson()
        val mPrefs = getPreferences(Context.MODE_PRIVATE)
        val userJson = intent.getStringExtra("User")
        if(userJson != null) {
            Log.v(TAG, "User: $userJson")
            user = gson.fromJson(userJson, User::class.java)
            addUser(user)

            mPrefs.edit().putString("user", user.id).apply()
        }
        else{
            val client = UserDatabaseClient(baseContext)
            launch(Dispatchers.Main) {
                user = client.getAllUsers()!!
            }
        }



        /** Creating and starting backgroundworker **/

        val data = Data.Builder()
        data.putString("userId", user.id)
        Log.v(TAG, "Starting periodic work")
        val request = PeriodicWorkRequestBuilder<ConnectionsApiService>(15, TimeUnit.MINUTES)
                .setInputData(data.build())
                .build()
        //WorkManager.getInstance(this).enqueue(request)
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "MyUniqueWorkName",
                ExistingPeriodicWorkPolicy.REPLACE,
                request)

        /*val instantRequest = OneTimeWorkRequestBuilder<ConnectionsApiService>().setInputData(data.build()).build()
        WorkManager.getInstance(this).enqueue(instantRequest)*/

        signalRSingleton.createSingleton(this)
        if(signalRSingleton.signalRClient.checkConnectionStatus() != "CONNECTED"){
            launch(Dispatchers.Main) {
                signalRSingleton.signalRClient.createConnection()
            }
        }

        launch(Dispatchers.Main){
            getInfectedIds(myContext)
        }

        try {
            val contactJson = intent.getStringExtra("contact")
            Log.v(TAG, "ContactJson: $contactJson")
            if (contactJson != "") {
                val contact = gson.fromJson(contactJson, Contact::class.java)
                showRequestDialog(this, contact)
            }
        } catch (e: Exception){
            Log.v(TAG, e.message.toString())
        }
    }

    object signalRSingleton{
        lateinit var signalRClient: SignalRClient
        fun createSingleton(context: Context) {
            signalRClient = SignalRClient(context)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private suspend fun getInfectedIds(context: Context){
        val dbClient = InfectedDatabaseClient(context)
        val response = ApiService.UserAdapter.tokenClient.getInfectedIds()
        if(response.isSuccessful && response.body() != null){
            val frontendInfected = dbClient.getAllInfectedIds()
            val backendInfected = ArrayList<InfectedUsers>()
            response.body()!!.forEach {
                backendInfected.add(InfectedUsers(UserId = it))
            }
            frontendInfected.forEach {
                if(!backendInfected.contains(it)){
                    Log.i(TAG, "User: ${it.UserId} not infected anymore.")
                    dbClient.deleteInfectedId(it)
                }
            }
            backendInfected.forEach {
                if(!frontendInfected.contains(it)){
                    Log.i(TAG, "New infection: ${it.UserId}.")
                    dbClient.addInfectedId(it)
                }
            }
        }
        else{
            Log.v(TAG, "Nice, no user is currently infected!")
        }
    }

    private fun addUser(user: User){
        launch(Dispatchers.Main){
            user.PasswordHash = ""
            user.location = ""
            user.name = ""
            user.dateOfInfection = null
            val client = UserDatabaseClient(this@MainActivity)
            val availableUser = client.getUserByEmail(user.eMail)
            if(availableUser == null) {
                client.addUser(user)
            }
            else{
                if(availableUser.name.isBlank()){
                    user.name = "bla"
                }
                user.name = availableUser.name
                if(availableUser.location.isBlank()){
                    user.location = "pandora"
                }
                client.updateUser(user)
            }
        }
    }

    private fun showRequestDialog(context: Context, contact: Contact) {
        Log.v(TAG, context.toString())
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_request)
        dialog.setCanceledOnTouchOutside(false)

        val acceptButton = dialog.findViewById(R.id.requestDialogAccept) as Button
        val rejectButton = dialog.findViewById(R.id.requestDialogReject) as Button

        val contactName = dialog.findViewById(R.id.requestDialogUsername) as TextView
        contactName.text = contact.username

        acceptButton.setOnClickListener {
            launch(Dispatchers.Main) {
                val contactDbClient = ContactDatabaseClient(context)
                contactDbClient.insert(contact)

                //send data to backend
                dialog.dismiss()
            }
        }
        rejectButton.setOnClickListener {
            launch(Dispatchers.Main){
                //send data to backend
                dialog.dismiss()
            }
        }
        dialog.show()
    }

}