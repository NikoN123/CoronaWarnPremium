package com.example.coronawarnpremium.ui.templates

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.coronawarnpremium.R

class ContactEncounterTemplate : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.template_contact_encounter)
        setSupportActionBar(findViewById(R.id.toolbar))

    }
}