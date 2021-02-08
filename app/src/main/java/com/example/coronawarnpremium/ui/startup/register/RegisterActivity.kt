package com.example.coronawarnpremium.ui.startup.register

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.coronawarnpremium.MainActivity
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.classes.RegisterRequest
import com.example.coronawarnpremium.classes.User
import com.example.coronawarnpremium.services.ApiService
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
    private var boxChecked = false

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
            boxChecked = isChecked
            watcher.onTextChanged("", 0, 0, 0)
        }
        username.addTextChangedListener(watcher)

        val termsTextView = findViewById<TextView>(R.id.termsTextView)
        termsTextView.setOnClickListener {
            showTermsDialog(this)
        }
    }

    private val watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(username.text.isEmpty() || email.text.isEmpty() || password.text.isEmpty() || !boxChecked){
                button.isEnabled = false
                Log.v(TAG, "Something is false")
            }
            else{
                button.isEnabled = true
            }
        }
        override fun afterTextChanged(s: Editable) {

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
                val response = ApiService.UserAdapter.userClient.registerAsync(request)
                Log.v(TAG, response.toString())
                if(response.isSuccessful && response.body() != null){
                    Log.v(TAG, "Registration successful")
                    val user = response.body()!!.user
                    user.token = response.body()!!.token
                    navigateMainActivity(view, user)
                    Log.v(TAG, user.toString())
                }
                else{
                    Log.v(TAG, response.message())
                }
                /*var user = User(
                        UserId = "",
                        Username = "Niko",
                        PasswordHash = "aljesfbaoqEBWF",
                        EMail = "nikoneigel@gmail.com",
                        Created = "03.01.2021",
                        Infected = false
                )*/
                //navigateMainActivity(view, user)
                //Toast.makeText(view.context, "Registered user with Id: ${user.UserId}.", Toast.LENGTH_SHORT).show()
            }
            catch (e: Exception){
                Toast.makeText(
                    this@RegisterActivity,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
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
    fun goBack(view: View){
        finish()
    }

    private fun showTermsDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_terms_conditions)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }
}

