package com.example.coronawarnpremium.ui.discovered.deviceclicked

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.coronawarnpremium.R

class DeviceClicked : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.device_clicked_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, DeviceClickedFragment.newInstance())
                .commitNow()
        }
    }
}