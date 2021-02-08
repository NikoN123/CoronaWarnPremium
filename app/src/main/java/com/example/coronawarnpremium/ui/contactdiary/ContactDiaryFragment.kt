package com.example.coronawarnpremium.ui.contactdiary

import android.app.Dialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.adapters.ContactAdapter
import com.example.coronawarnpremium.adapters.EncounterAdapter
import com.example.coronawarnpremium.classes.Contact
import com.example.coronawarnpremium.classes.PersonContactDiary
import com.example.coronawarnpremium.services.DashboardService
import com.example.coronawarnpremium.storage.contact.ContactDatabaseClient
import com.example.coronawarnpremium.storage.contactdiary.ContactDiaryDatabaseClient
import com.example.coronawarnpremium.ui.contactbook.ItemTouchListener
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "ContactDiaryFragment"
class ContactDiaryFragment : Fragment(), CoroutineScope by MainScope(), ItemTouchListener.OnRecyclerClickListener  {

    private lateinit var viewModel: ContactDiaryViewModel
    val recyclerViewAdapter = EncounterAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var context = activity
        viewModel =
                ViewModelProvider(this).get(ContactDiaryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_contact_diary, container, false)

        val recyclerView = root.findViewById<RecyclerView>(R.id.encounterRecyclerview)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        activity?.let { ItemTouchListener(it, recyclerView, this) }?.let { recyclerView.addOnItemTouchListener(it) }
        recyclerView.adapter = recyclerViewAdapter

        activity?.baseContext?.let { loadData(it) }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ContactDiaryViewModel::class.java)
    }

    private fun loadData(context: Context){
        launch(Dispatchers.Main){
            val client = ContactDiaryDatabaseClient(context)
            EncounterAdapter.encounters = client.getAllContacts()
            client.destroy()
            notifyAdapter()
        }
    }
    private suspend fun notifyAdapter(){
        recyclerViewAdapter.reloadData()
    }


    override fun OnItemClick(view: View, position: Int) {
        Log.v(TAG, "Click at position: $position")
        val encounter = recyclerViewAdapter.getEncounter(position)
        showDialog(view, encounter)
    }

    //TODO: maybe add long click menu for delete
    override fun OnItemLongClick(view: View, position: Int) {
        Log.v(TAG, "Long click at position: $position")
    }

    //TODO: create new dialog and change logic
    private fun showDialog(view: View, encounter: PersonContactDiary) {
        Log.v(TAG, "Encounter: ${encounter.UserId}")
        val dialog = activity?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.dialog_edit_encounter)
        dialog?.setCanceledOnTouchOutside(true)
        val saveButton = dialog?.findViewById(R.id.dialogSaveEncounterChanges) as Button
        val mail = dialog.findViewById(R.id.dialogEncounterEmail) as EditText
        mail.setText(encounter.EMail)
        val name = dialog.findViewById(R.id.dialogEncounterUsername) as EditText
        name.setText(encounter.Name)
        val location = dialog.findViewById(R.id.dialogEncounterLocation) as EditText
        location.setText(encounter.Location)
        saveButton.setOnClickListener {
            launch(Dispatchers.Main) {
                encounter.Name = name.text.toString()
                encounter.EMail = mail.text.toString()
                encounter.Location = location.text.toString()
                val dbClient = ContactDiaryDatabaseClient(view.context)
                dbClient.update(encounter)
                EncounterAdapter.encounters = dbClient.getAllContacts()
                notifyAdapter()
                Log.v(TAG, "Encounter: ${encounter.Name} edited.")
                dbClient.destroy()
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}