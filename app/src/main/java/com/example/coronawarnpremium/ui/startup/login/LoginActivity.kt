package com.example.coronawarnpremium.ui.startup.login

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.coronawarnpremium.MainActivity
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.classes.User
import com.example.coronawarnpremium.ui.startup.register.RegisterActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*


private const val ENABLE_BLUETOOTH_REQUEST_CODE = 1
private const val LOCATION_PERMISSION_REQUEST_CODE = 2
private const val TAG = "LoginActivity"
class LoginActivity : AppCompatActivity(), CoroutineScope by MainScope()  {
    private lateinit var id:String

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    private val isLocationPermissionGranted
        get() = hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    val phonePermission
            get() = hasPermission(Manifest.permission.READ_PHONE_STATE)

    private lateinit var email: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(findViewById(R.id.toolbar))

        email = findViewById(R.id.emailRegister)
        password = findViewById(R.id.passwordRegister)

        /*
         * Check and get permissions if needed
         */
        if (!bluetoothAdapter.isEnabled) {
            promptEnableBluetooth()
        }
        if (!isLocationPermissionGranted) {
            requestLocationPermission()
        }
        Log.v(TAG, "Permission: $phonePermission")
        if(!phonePermission){
            requestTelephonyPermission()
        }
        id = UUID.randomUUID().toString()
    }

    fun login(view: View){
        launch(Dispatchers.Main) {
            try{
                Log.v(TAG, "Attempting to login")
                /*val response = UserService.UserAdapter.userClient.loginAsync(email.text.toString(), password.text.toString())
                if(response.isSuccessful && response.body() != null){
                    navigateMainActivity(view, response.body()!!)
                }*/
                val user = User(
                    UserId = id,
                    Username = "Niko",
                    PasswordHash = "aljesfbaoqEBWF",
                    EMail = "nikoneigel@gmail.com",
                    Created = "03.01.2021",
                    Infected = false
                )
                Log.v(TAG, "Login successful")
                navigateMainActivity(view, user)
            }
            catch(e: Exception){
                Toast.makeText(this@LoginActivity,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    fun navigateMainActivity(view: View, user: User){
        val gson = Gson()
        val json = gson.toJson(user)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("User", json)
        startActivity(intent)
    }
    fun navigateRegisterActivity(view: View){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }



    private fun requestLocationPermission() {
        if (isLocationPermissionGranted) {
            return
        }
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
    }
    private fun requestTelephonyPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), 1)
    }

    private fun Activity.requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    }

    private fun Context.hasPermission(permissionType: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permissionType) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun promptEnableBluetooth() {
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE)
        }
    }
}