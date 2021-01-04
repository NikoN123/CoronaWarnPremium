package com.example.coronawarnpremium.ui.discovered.deviceclicked

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.ui.discovered.DiscoveredDevicesViewModel

class DeviceClickedFragment : Fragment() {

    companion object {
        fun newInstance() =
            DeviceClickedFragment()
    }

    private lateinit var viewModel: DeviceClickedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(DeviceClickedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_device_clicked, container, false)
        return root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DeviceClickedViewModel::class.java)
        // TODO: Use the ViewModel
    }

}