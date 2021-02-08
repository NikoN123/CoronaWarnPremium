package com.example.coronawarnpremium.ui.account

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.classes.User
import com.example.coronawarnpremium.rest.interfaces.ApiClient
import com.example.coronawarnpremium.services.ApiService
import com.example.coronawarnpremium.storage.user.UserDatabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private const val TAG = "AccountFragment"
class AccountFragment : Fragment(), CoroutineScope by MainScope() {

    private lateinit var username: EditText
    private lateinit var name: EditText
    private lateinit var location: EditText
    private lateinit var email: EditText
    private lateinit var saveButton: Button
    private lateinit var user: User

    companion object {
        fun newInstance() = AccountFragment()
    }

    private lateinit var viewModel: AccountViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_account, container, false)

        username = root.findViewById(R.id.accountInfoUsername)
        email = root.findViewById(R.id.accountInfoEmail)
        saveButton = root.findViewById(R.id.accountInfoButton)
        name = root.findViewById(R.id.accountInfoName)
        location = root.findViewById(R.id.accountInfoLocation)

        saveButton.setOnClickListener {
            saveChanges(it)
        }

        launch(Dispatchers.Main) {
            getUserData()
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private suspend fun getUserData(){
            try {
                val client = UserDatabaseClient(requireActivity().baseContext)
                user = client.getAllUsers()!!
                username.setText(user.username)
                email.setText(user.eMail)
                name.setText(user.name)
                location.setText(user.location)
                Log.e(TAG, user.toString())
            } catch(e: Exception){
                Toast.makeText(context,
                        "Error Occurred: ${e.message}",
                        Toast.LENGTH_LONG).show()
            }
    }

    private fun saveChanges(view: View){
        launch(Dispatchers.Main) {
            try {
                user.username = username.text.toString()
                user.eMail = email.text.toString()
                user.location = location.text.toString()
                user.name = name.text.toString()
                user.dateOfInfection = null
                ApiService.UserAdapter.setUserToken(user.token)
                val x = USer(id=user.id, username = user.username, eMail = user.eMail, name=user.name, location = user.location, created = user.created, dateOfInfection = user.dateOfInfection, infected = user.infected)
                val response = ApiService.UserAdapter.tokenClient.patchUser(user.id, x)
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(view.context, "User updated.", Toast.LENGTH_SHORT).show()
                    val client = UserDatabaseClient(view.context)
                    client.updateUser(user)
                }
                else{
                    Toast.makeText(view.context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }catch(e: Exception){
                Toast.makeText(view.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

class USer(
    var id: String,
    var username: String,
    var eMail: String,
    var name: String,
    var location: String,
    var created: String,
    var dateOfInfection: String?,
    var infected: Boolean
)