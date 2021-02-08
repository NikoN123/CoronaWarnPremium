package com.example.coronawarnpremium.ui.startup.register

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.coronawarnpremium.R

class TermsConditionsDialog : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_terms_conditions)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}