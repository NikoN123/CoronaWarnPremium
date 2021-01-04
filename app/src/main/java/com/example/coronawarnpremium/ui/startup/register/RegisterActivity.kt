package com.example.coronawarnpremium.ui.startup.register

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coronawarnpremium.MainActivity
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.classes.RegisterRequest
import com.example.coronawarnpremium.classes.User
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

private const val TAG ="RegisterActivity"
class RegisterActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var checkbox: CheckBox
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(findViewById(R.id.toolbar))

        username = findViewById(R.id.usernameRegister)
        email = findViewById(R.id.emailRegister)
        password = findViewById(R.id.passwordRegister)
        button = findViewById(R.id.registerButton)
        checkbox = findViewById(R.id.acknowledgeCheckbox)

        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            button.isEnabled = isChecked
            Log.v(TAG, "Checkbox checked")
        }
    }

    fun register(view: View){
        val request = RegisterRequest()
        request.EMail = email.text.toString()
        request.Username = username.text.toString()
        request.Password = password.text.toString()
        launch(Dispatchers.Main) {
            try{
                Log.v(TAG, "Attempting to register")
                /*val user = UserService.UserAdapter.userClient.registerAsync(request)
                if(user.isSuccessful){
                    //set user to sharedpref
                    var prefsEditor = mPrefs.edit()
                    var gson = Gson()
                    prefsEditor.putString("User", gson.toJson(user))
                    prefsEditor.apply()
                    navigate(view)
                }*/
                var user = User(
                        UserId = "",
                        Username = "Niko",
                        PasswordHash = "aljesfbaoqEBWF",
                        IMEI = "23:EO:10:B1:00",
                        EMail = "nikoneigel@gmail.com",
                        Created = "03.01.2021"
                )
                Log.v(TAG, "Registration successful")
                navigateMainActivity(view, user)
            }
            catch(e: Exception){
                Toast.makeText(this@RegisterActivity,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun navigateMainActivity(view: View, user: User){
        val gson = Gson()
        val json = gson.toJson(user)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("User", json)
        startActivity(intent)
    }
}

