package com.example.coronawarnpremium.ui.discovered

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DiscoveredDevicesViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is discovered devices Fragment"
    }
    val text: LiveData<String> = _text
}