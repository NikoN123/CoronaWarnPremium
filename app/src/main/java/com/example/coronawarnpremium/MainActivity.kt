package com.example.coronawarnpremium

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import androidx.work.*
import com.example.coronawarnpremium.classes.User
import com.example.coronawarnpremium.services.ConnectionsApiService
import com.example.coronawarnpremium.storage.user.UserDao
import com.example.coronawarnpremium.storage.user.UserDatabase
import com.example.coronawarnpremium.storage.user.UserDatabaseClient
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
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
                R.id.nav_home, R.id.nav_gallery, R.id.nav_discovered_devices, R.id.nav_contact_book, R.id.nav_contact_diary), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val userJson = intent.getStringExtra("User")
        val gson = Gson()
        val user = gson.fromJson(userJson, User::class.java)
        addUser(user)

        val mPrefs = getPreferences(Context.MODE_PRIVATE)
        mPrefs.edit().putString("user", user.UserId).apply()

        val data = Data.Builder()
        data.putString("userId", user.UserId)

        val constraint = Constraints.Builder().setRequiresDeviceIdle(false).build()

        Log.v(TAG, "Starting periodic work")
        val request = PeriodicWorkRequestBuilder<ConnectionsApiService>(15, TimeUnit.MINUTES)
                .setConstraints(constraint)
                .setInputData(data.build())
                .build()
        //WorkManager.getInstance(this).enqueue(request)
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "MyUniqueWorkName",
                ExistingPeriodicWorkPolicy.REPLACE,
                request)
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

    private fun addUser(user: User){
        launch(Dispatchers.Main){
            var client = UserDatabaseClient(this@MainActivity)
            client.addUser(user)
        }
    }

    /** Start scan when app is reopened **/
    override fun onStart() {
        super.onStart()

        val mPrefs = getPreferences(Context.MODE_PRIVATE)
        val userId = mPrefs.getString("user", "")

        if(userId != "") {
            val data = Data.Builder()
            data.putString("userId", userId)

            val instantWorker = OneTimeWorkRequestBuilder<ConnectionsApiService>()
                .setInputData(data.build())
                .build()
            WorkManager.getInstance(this).enqueueUniqueWork("BluetoothScan", ExistingWorkPolicy.KEEP, instantWorker)
            Log.v(TAG, "Starting instant worker")
        }
    }

}