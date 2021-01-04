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

    suspend fun getHealthTips():ArrayList<String>{
        //set list
        tips.add("1. Keep distance.")
        tips.add("2. Don't spit on other people.")
        tips.add("3. Don't sneeze other people in their face.")
        tips.add("4. Don't be a #COVIDIOT")
        tips.add("5. Don't trust conspiracy theorists")
        tips.add("6. Most importantly, stay away from Mosbach!")
        return tips
    }

    suspend fun checkUserRiskEncounters():Boolean{
        Log.v(TAG, "Getting risk encounters")
        //check if there are people with rona in your contact book
        return risk
    }
}