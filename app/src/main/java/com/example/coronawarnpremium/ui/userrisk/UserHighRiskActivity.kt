package com.example.coronawarnpremium.ui.userrisk

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.coronawarnpremium.R

class UserHighRiskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_high_risk)
        setSupportActionBar(findViewById(R.id.toolbar))


    }

    fun closeActivity(view:View){
        finish()
    }
}