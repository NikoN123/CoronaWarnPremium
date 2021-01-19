package com.example.coronawarnpremium.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.classes.PersonContactDiary
import com.example.coronawarnpremium.viewholders.EncounterViewHolder

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
        holder.encounterID.setText(encounters[position].UserId)
        holder.encounterUsername.setText(encounters[position].Name)
        holder.encounterEmail.setText(encounters[position].EMail)
        holder.encounterLocation.setText(encounters[position].Location)
        holder.encounterDate.text = (encounters[position].EncounterDate.toString())
        holder.encounterTime.text = (encounters[position].EncounterTime)
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

    fun reloadData(){
        notifyDataSetChanged()
    }
}