package com.example.coronawarnpremium.ui.contactbook

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawarnpremium.MainActivity
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.adapters.ContactAdapter
import com.example.coronawarnpremium.classes.Contact
import com.example.coronawarnpremium.classes.User
import com.example.coronawarnpremium.services.ApiService
import com.example.coronawarnpremium.storage.contact.ContactDatabaseClient
import com.example.coronawarnpremium.storage.user.UserDatabaseClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private const val TAG = "ContactBookFragment"
class ContactBookFragment : Fragment(), CoroutineScope by MainScope(), ItemTouchListener.OnRecyclerClickListener {
    val recyclerViewAdapter = ContactAdapter()
    private lateinit var viewModel: ContactBookViewModel
    private lateinit var user: User

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val context = activity
        viewModel =
            ViewModelProvider(this).get(ContactBookViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_contact_book, container, false)

        root.findViewById<FloatingActionButton>(R.id.fabContact).setOnClickListener { view ->
            showDialog(view)
        }

        launch(Dispatchers.Main) {
            val dbClient = UserDatabaseClient(root.context)
            user = dbClient.getAllUsers()!!
        }

        val recyclerView = root.findViewById<RecyclerView>(R.id.contactsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        activity?.let { ItemTouchListener(it, recyclerView, this) }?.let { recyclerView.addOnItemTouchListener(it) }
        recyclerView.adapter = recyclerViewAdapter

        loadData()

        return root
    }

    override fun OnItemClick(view: View, position: Int) {
        Log.v(TAG, "Click at position: $position")
        val contact = recyclerViewAdapter.getContact(position)
        val gson = Gson()
        val json = gson.toJson(contact)
        Log.v(TAG, "Creating bottom sheet")
        /*activity?.supportFragmentManager.let {
            ContactInfoTemplate.newInstance(Bundle(), json).apply {
                if (it != null) {
                    show(it, tag)
                }

            }
        }*/
        /*val bottomSheet = ContactInfoTemplate.newInstance(Bundle(), json)
        activity?.supportFragmentManager?.let {
            bottomSheet.show(it, tag)
        }*/

        showDeleteDialog(view, contact)

    }

    fun onItemCLick(view: View, param: String){
        Log.v(TAG, "Delete button clicked with param: $param")
        launch(Dispatchers.Main) {
            val client = ContactDatabaseClient(view.context)
            val contact = client.getContact(param)
            client.deleteContact(contact)
            ContactAdapter.contacts = client.getAllContacts()
            onDataAvailable()
            //loadData()
        }
    }

    override fun OnItemLongClick(view: View, position: Int) {
        Log.v(TAG, "Long click at position: $position")
    }

    private fun loadData(){
        launch(Dispatchers.Main){
            context?.let { loadContacts(it) }
        }
    }

    suspend fun loadContacts(context: Context){
        Log.v(TAG, "Loading contacts...")
        try {
            val client = ContactDatabaseClient(context)
            ContactAdapter.contacts = client.getAllContacts()
            onDataAvailable()
        } catch (e: Exception){
            throw e
        }
    }

    private fun onDataAvailable(){
        recyclerViewAdapter.reloadData()
    }

    private fun showDialog(view: View) {
        val dialog = activity?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.dialog_add_contact)
        val email = dialog?.findViewById(R.id.addContactEmail) as EditText
        val sendButton = dialog.findViewById(R.id.sendContactRequestButton) as Button
        val dismissButton = dialog.findViewById(R.id.closeContactRequestButton) as Button
        sendButton.setOnClickListener {
            launch(Dispatchers.Main) {
                Log.v(TAG, "Sending request")
                try {
                    ApiService.UserAdapter.setUserToken(user.token)
                    val response =
                        ApiService.UserAdapter.tokenClient.sendContactRequest(user.id, email.text.toString())
                    Log.v(TAG, "Request sent")
                    if(response.isSuccessful && response.body() != null){
                        dialog.dismiss()
                        Snackbar.make(view, resources.getString(R.string.contactRequestSuccessful), Snackbar.LENGTH_LONG)
                            .show()
                        MainActivity.signalRSingleton.signalRClient.checkConnectionStatus()
                    }
                    else if(response.isSuccessful && response.body() == null){
                        dialog.dismiss()
                        Snackbar.make(view, response.message(), Snackbar.LENGTH_LONG)
                            .show()
                    }
                    else{
                        dialog.dismiss()
                        Snackbar.make(view, response.message(), Snackbar.LENGTH_LONG)
                            .show()
                    }
                } catch (e: Exception){
                    e.message?.let { it1 ->
                        Snackbar.make(view, it1, Snackbar.LENGTH_LONG)
                            .show()
                    }
                }
                /*val contact = Contact()
                contact.email = "nikoneigel@gmail.com"
                contact.username = "Niko Neigel"
                contact.id = "ce7c6b92-c7a6-4ab8-9e42-27b642a485b4"
                val client = ContactDatabaseClient(requireActivity())
                client.insert(contact)
                loadData()
                Log.v(TAG, "Contact with username: ${contact.username}, email: ${contact.email} and id: ${contact.id} added.")
                dialog.dismiss()*/
            }
        }
        dismissButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showDeleteDialog(view: View, contact: Contact) {
        val dialog = activity?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.template_contact_info)
        dialog?.setCanceledOnTouchOutside(true)
        val deleteButton = dialog?.findViewById(R.id.deleteContact) as Button
        val contactName = dialog.findViewById(R.id.contactInfoName) as TextView
        contactName.text = contact.username
        val contactMail = dialog.findViewById(R.id.contactInfoMail) as TextView
        contactMail.text = contact.email
        deleteButton.setOnClickListener {
            launch(Dispatchers.Main) {
                val contactDbClient = ContactDatabaseClient(view.context)
                contactDbClient.deleteContact(contact)
                ContactAdapter.contacts = contactDbClient.getAllContacts()
                //onDataAvailable()
                loadData()
                Log.v(TAG, "Contact ${contact.username} deleted.")
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}