package com.example.coronawarnpremium.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.classes.InfectedUsers
import com.example.coronawarnpremium.classes.PersonContactDiary
import com.example.coronawarnpremium.services.DashboardService
import com.example.coronawarnpremium.storage.contact.ContactDatabaseClient
import com.example.coronawarnpremium.storage.contactdiary.ContactDiaryDatabaseClient
import com.example.coronawarnpremium.storage.infected.InfectedDatabaseClient
import com.example.coronawarnpremium.viewholders.EncounterViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

private const val TAG = "EncounterAdapter"
class EncounterAdapter : RecyclerView.Adapter<EncounterViewHolder>(), CoroutineScope by MainScope() {
    private var infected: ArrayList<InfectedUsers> = ArrayList()

    companion object{
        var encounters = ArrayList<PersonContactDiary>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EncounterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.template_contact_encounter, parent, false)
        return EncounterViewHolder(view)
    }

    override fun onBindViewHolder(holder: EncounterViewHolder, position: Int) {
        if(!encounters[position].Merged){
            mergeEncounter(position, holder.itemView.context)
        }

        launch(Dispatchers.Main){
            val infectedClient = InfectedDatabaseClient(holder.itemView.context)
            val infections = infectedClient.getAllInfectedIds()

            val formattedDate = encounters[position].EncounterDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            holder.encounterID.text = encounters[position].UserId
            holder.encounterUsername.text = encounters[position].Name
            holder.encounterEmail.text = encounters[position].EMail
            holder.encounterLocation.text = encounters[position].Location
            holder.encounterDate.text = formattedDate
            holder.encounterTime.text = encounters[position].EncounterTime

            infections.forEach {
                if(encounters[position].UserId == it.UserId){
                    holder.encounterID.setTextColor(Color.RED)
                    holder.encounterUsername.setTextColor(Color.RED)
                    holder.encounterEmail.setTextColor(Color.RED)
                    holder.encounterLocation.setTextColor(Color.RED)
                    holder.encounterDate.setTextColor(Color.RED)
                    holder.encounterTime.setTextColor(Color.RED)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return if (encounters.isNotEmpty()) {
            encounters.size
        } else{
            0
        }
    }

    fun getEncounter(position: Int): PersonContactDiary{
        return encounters[position]
    }

    fun reloadData(){
        Log.v(TAG, "Number of encounters: ${encounters.size}")
        notifyDataSetChanged()
    }

    private fun mergeEncounter(position: Int, context: Context){
        val contactDiaryClient = ContactDiaryDatabaseClient(context)
        val contactClient = ContactDatabaseClient(context)

        launch(Dispatchers.Main) {
            val contact = contactClient.getContact(encounters[position].UserId)
            if (contact == null) {
                Log.v(TAG, "Contact: ${encounters[position].UserId} not in db.")
            } else {
                Log.v(TAG, "Merging encounter: ${encounters[position].UserId}.")
                val encounter = encounters[position]
                encounter.Name = contact.username
                encounter.EMail = contact.email
                encounter.Merged = true
                contactDiaryClient.update(encounter)
                reloadData()
            }
        }
    }
}