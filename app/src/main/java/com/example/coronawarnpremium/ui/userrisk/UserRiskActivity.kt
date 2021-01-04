package com.example.coronawarnpremium.ui.userrisk

import android.os.Bundle
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.coronawarnpremium.R

class UserRiskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_risk)
        setSupportActionBar(findViewById(R.id.toolbar))


    }

    fun closeActivity(view: View){
        finish()
    }
}