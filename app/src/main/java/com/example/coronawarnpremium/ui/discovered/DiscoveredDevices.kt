package com.example.coronawarnpremium.ui.discovered

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.coronawarnpremium.R

class DiscoveredDevices : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.discovered_devices_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, DiscoveredDevicesFragment.newInstance())
                .commitNow()
        }
    }
}