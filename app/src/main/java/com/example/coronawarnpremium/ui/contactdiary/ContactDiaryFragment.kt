package com.example.coronawarnpremium.ui.contactdiary

import android.content.Context
import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.adapters.EncounterAdapter
import com.example.coronawarnpremium.classes.PersonContactDiary
import com.example.coronawarnpremium.storage.contactdiary.ContactDiaryDatabaseClient
import com.example.coronawarnpremium.ui.contactbook.ItemTouchListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ContactDiaryFragment : Fragment(), CoroutineScope by MainScope() {

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
            onDataAvailable()
        }
    }

    private fun onDataAvailable(){
        recyclerViewAdapter.reloadData()
    }

}