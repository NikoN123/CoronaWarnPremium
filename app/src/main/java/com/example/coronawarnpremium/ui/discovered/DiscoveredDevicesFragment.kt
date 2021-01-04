package com.example.coronawarnpremium.ui.discovered

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.adapters.BluetoothOverviewAdapter

private const val TAG = "DiscoveredDevicesFragment"

class DiscoveredDevicesFragment : Fragment() {
    private val recyclerViewAdapter = BluetoothOverviewAdapter(ArrayList())

    companion object {
        fun newInstance() =
            DiscoveredDevicesFragment()
    }

    private lateinit var viewModel: DiscoveredDevicesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(DiscoveredDevicesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_discovered_devices, container, false)

        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = recyclerViewAdapter

        //add devices from storage to recyclerview

        //call onDataAvailable with all devices from storage
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DiscoveredDevicesViewModel::class.java)

    }

    //notify frontend about new data being found
    fun onDataAvailable(data: ArrayList<BluetoothDevice>){
        recyclerViewAdapter.loadNewData(data)
    }
}