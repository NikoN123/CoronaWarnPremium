package com.example.coronawarnpremium.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.classes.PersonContactDiary
import com.example.coronawarnpremium.viewholders.EncounterViewHolder
import java.time.format.DateTimeFormatter

private const val TAG = "EncounterAdapter"
class EncounterAdapter : RecyclerView.Adapter<EncounterViewHolder>() {

    companion object{
        var encounters = ArrayList<PersonContactDiary>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EncounterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.template_contact_encounter, parent, false)
        return EncounterViewHolder(view)
    }

    override fun onBindViewHolder(holder: EncounterViewHolder, position: Int) {
        val formattedDate = encounters[position].EncounterDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        holder.encounterID.text = encounters[position].UserId
        holder.encounterUsername.text = encounters[position].Name
        holder.encounterEmail.text = encounters[position].EMail
        holder.encounterLocation.text = encounters[position].Location
        holder.encounterDate.text = formattedDate
        holder.encounterTime.text = encounters[position].EncounterTime
    }

    override fun getItemCount(): Int {
        return if (encounters.isNotEmpty()) {
            encounters.size
        } else{
            0
        }
    }

    fun removeEncounter(person: PersonContactDiary){
        val index = encounters.indexOfFirst { it.UserId == person.UserId }
        encounters.removeAt(index)
        notifyDataSetChanged()
    }

    fun addNewData(person: PersonContactDiary){
        if(!encounters.contains(person)) {
            encounters.add(person)
            Log.v(TAG, "Encounter added")
            notifyDataSetChanged()
        }
    }

    fun getEncounter(position: Int): PersonContactDiary{
        return encounters[position]
    }

    fun reloadData(){
        Log.e(TAG, "Number of encounters: ${encounters.size}")
        notifyDataSetChanged()
    }
}