package com.example.coronawarnpremium

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
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.coronawarnpremium.classes.User
import com.example.coronawarnpremium.services.ConnectionsApiService
import com.example.coronawarnpremium.storage.user.UserDao
import com.example.coronawarnpremium.storage.user.UserDatabase
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
    private lateinit var db: UserDatabase
    private lateinit var userDao: UserDao

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



        val constraint = Constraints.Builder().setRequiresDeviceIdle(false).build()
        // instantly call worker
        //val instantWork = OneTimeWorkRequestBuilder<BluetoothDiscoveryService>().build()
        //WorkManager.getInstance(this).enqueueUniqueWork("InstantBluetoothScan", ExistingWorkPolicy.KEEP, instantWork)

        /*Log.v(TAG, "Starting periodic work")
        val request = PeriodicWorkRequestBuilder<BluetoothDiscoveryService>(15, TimeUnit.MINUTES)
                .setConstraints(constraint)
                .build()
        //WorkManager.getInstance(this).enqueue(request)
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "MyUniqueWorkName",
                ExistingPeriodicWorkPolicy.REPLACE,
                request)*/

        val userJson = intent.getStringExtra("User")
        val gson = Gson()
        val user = gson.fromJson(userJson, User::class.java)

        launch(Dispatchers.Main){
            val service = ConnectionsApiService(this@MainActivity, user.UserId)
            Log.v(TAG, "Launching service")
            service.startConnectionsService()
        }

        launch(Dispatchers.Main) {
            Log.v(TAG, "Creating database")
            createUserDatabase()
            if(userDao.getUser(user.IMEI) == null){
                Log.v(TAG, "Attempting to save user")
                userDao.insert(user)
            }
            else {
                Log.v(TAG, "Updating user...")
                userDao.update(user)
            }
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

    suspend fun createUserDatabase(){
        db = Room.databaseBuilder(this, UserDatabase::class.java, "eiv").build()
        userDao = db.userDao
        Log.v(TAG, "Database successfully created")
    }

}