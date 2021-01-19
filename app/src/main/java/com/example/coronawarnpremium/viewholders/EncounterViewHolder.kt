package com.example.coronawarnpremium.viewholders

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawarnpremium.R

class EncounterViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    var encounterID: EditText = view.findViewById(R.id.encounterUserId)
    var encounterUsername: EditText = view.findViewById(R.id.encounterUsername)
    var encounterEmail: EditText = view.findViewById(R.id.encounterEmail)
    var encounterLocation: EditText = view.findViewById(R.id.encounterLocation)
    var encounterDate: TextView = view.findViewById(R.id.encounterDate)
    var encounterTime: TextView = view.findViewById(R.id.encounterTime)
}