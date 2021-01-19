package com.example.coronawarnpremium.services

import android.content.Context
import android.util.Log

private const val TAG = "DashboardService"
class DashboardService {
    private var tips = ArrayList<String>()
    //private var user: User = User{}
    private var infectionStatus = false
    private var risk = true

    suspend fun getInfectionStatus():Boolean{
        Log.v(TAG, "Getting infection status")
        //get infection status
        infectionStatus = false
        return infectionStatus
    }

    suspend fun checkUserRiskEncounters():Boolean{
        Log.v(TAG, "Getting risk encounters")
        //check if there are people with rona in your contact book
        return risk
    }
}