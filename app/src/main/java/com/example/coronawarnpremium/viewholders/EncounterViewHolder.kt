package com.example.coronawarnpremium.viewholders

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawarnpremium.R

class EncounterViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    var encounterID: TextView = view.findViewById(R.id.encounterUserId)
    var encounterUsername: TextView = view.findViewById(R.id.dialogEncounterUsername)
    var encounterEmail: TextView = view.findViewById(R.id.dialogEncounterEmail)
    var encounterLocation: TextView = view.findViewById(R.id.dialogEncounterLocation)
    var encounterDate: TextView = view.findViewById(R.id.encounterDate)
    var encounterTime: TextView = view.findViewById(R.id.encounterTime)
}