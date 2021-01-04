package com.example.coronawarnpremium.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawarnpremium.R

class BluetoothOverviewViewHolder(view: View): RecyclerView.ViewHolder(view) {
    var deviceName: TextView = view.findViewById(R.id.deviceName)
    var deviceIMEI: TextView = view.findViewById(R.id.deviceIMEI)
    var deviceId: TextView = view.findViewById(R.id.deviceId)
}