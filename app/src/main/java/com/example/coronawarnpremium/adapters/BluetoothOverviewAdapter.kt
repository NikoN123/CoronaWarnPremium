package com.example.coronawarnpremium.adapters

import android.bluetooth.BluetoothDevice
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.viewholders.BluetoothOverviewViewHolder

class BluetoothOverviewAdapter(private var deviceList: ArrayList<BluetoothDevice>) : RecyclerView.Adapter<BluetoothOverviewViewHolder>() {
    private val TAG = "CustomBLEAdapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothOverviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_bluetooth_overview_card, parent, false)
        return BluetoothOverviewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (deviceList.isNotEmpty()) {
            deviceList.size
        } else{
            0
        }
    }

    fun addNewData(device: BluetoothDevice){
        if(!deviceList.contains(device)) {
            deviceList.add(device)
            Log.v(TAG, "Device added")
            notifyDataSetChanged()
        }
    }

    fun loadNewData(newList : ArrayList<BluetoothDevice>){
        deviceList = newList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BluetoothOverviewViewHolder, position: Int) {
        holder.deviceName.text = deviceList[position].name
        holder.deviceIMEI.text = deviceList[position].address
        //holder.deviceId.text = deviceList[position].uuids.toString()
    }
}