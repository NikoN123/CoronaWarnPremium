package com.example.coronawarnpremium.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawarnpremium.R

class ContactViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    var contactUsername: TextView = view.findViewById(R.id.userNameContactCard)
    var contactEmail: TextView = view.findViewById(R.id.emailContactCard)
}