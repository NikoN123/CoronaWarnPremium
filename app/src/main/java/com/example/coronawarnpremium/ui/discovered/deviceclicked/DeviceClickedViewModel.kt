package com.example.coronawarnpremium.ui.discovered.deviceclicked

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DeviceClickedViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is clicked devices Fragment"
    }
    val text: LiveData<String> = _text
}