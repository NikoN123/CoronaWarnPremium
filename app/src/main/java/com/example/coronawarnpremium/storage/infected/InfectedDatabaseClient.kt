package com.example.coronawarnpremium.storage.infected

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.coronawarnpremium.classes.InfectedUsers
import com.example.coronawarnpremium.classes.User

private const val TAG = "InfectedDatabaseClient"
class InfectedDatabaseClient( context: Context) {
    private var db: InfectedDatabase = Room.databaseBuilder(context, InfectedDatabase::class.java, "infected_db").build()
    private var infectedDao: InfectedDao = db.infectedDao

    suspend fun addInfectedId(user: InfectedUsers){
            Log.v(TAG, "Attempting to save infected id")
            try {
                infectedDao.insert(user)
            } catch (e: Exception){
                throw e
            }
    }
    suspend fun getAllInfectedIds(): List<InfectedUsers>{
        Log.v(TAG, "Attempting to get all infected ids")
        try {
            return infectedDao.getAllInfectedIds()
        } catch (e: Exception){
            throw e
        }
    }
    suspend fun deleteInfectedId(user: InfectedUsers){
        Log.v(TAG, "Attempting to delete infected id")
        try {
            infectedDao.delete(user)
        } catch (e: Exception){
            throw e
        }
    }
    suspend fun clearInfectedIds(){
        Log.v(TAG, "Attempting to delete infected_db")
        try {
            infectedDao.clear()
        } catch (e: Exception){
            throw e
        }
    }
    fun destroy(){
        db.close()
    }
}