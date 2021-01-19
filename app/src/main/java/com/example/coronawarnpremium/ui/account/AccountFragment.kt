package com.example.coronawarnpremium.ui.account

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.classes.User
import com.example.coronawarnpremium.storage.user.UserDatabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "AccountFragment"
class AccountFragment : Fragment(), CoroutineScope by MainScope() {

    private lateinit var username: EditText
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
                username.setText(user.Username)
                email.setText(user.EMail)
            } catch(e: Exception){
                Toast.makeText(context,
                        "Error Occurred: ${e.message}",
                        Toast.LENGTH_LONG).show()
            }
    }

    private fun saveChanges(view: View){
        launch(Dispatchers.Main) {
            val client = UserDatabaseClient(view.context)
            user.Username = username.text.toString()
            user.EMail = email.text.toString()
            client.updateUser(user)
        }
    }

}