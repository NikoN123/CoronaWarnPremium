package com.example.coronawarnpremium.services

import android.content.Context
import android.util.Log
import com.example.coronawarnpremium.storage.contactdiary.ContactDiaryDatabaseClient
import com.example.coronawarnpremium.storage.infected.InfectedDatabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private const val TAG = "DashboardService"
class DashboardService {
    suspend fun checkUserRiskEncounters(context: Context):Boolean{
        Log.v(TAG, "Getting risk encounters")
        //check if there are people with rona in your contact book
        val contactDiaryClient = ContactDiaryDatabaseClient(context)
        val infectedClient = InfectedDatabaseClient(context)
        val infections = infectedClient.getAllInfectedIds()

        if(infections.isEmpty()){
            return false
        }
        else{
            val contacts = contactDiaryClient.getAllContacts()
            infections.forEach{ inf ->
                val id = inf.UserId
                contacts.forEach {
                    if(it.UserId == id){
                        return true
                    }
                }
            }
        }
        return false
    }
}