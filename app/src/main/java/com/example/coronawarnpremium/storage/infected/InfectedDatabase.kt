package com.example.coronawarnpremium.storage.infected

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.coronawarnpremium.classes.Contact
import com.example.coronawarnpremium.classes.InfectedUsers
import com.example.coronawarnpremium.classes.User

@Database(entities=[InfectedUsers::class],version=1,exportSchema = false)
abstract class InfectedDatabase : RoomDatabase() {
    //creating an abstract instance of the database dao
    abstract val infectedDao: InfectedDao

    companion object {
        @Volatile
        private var INSTANCE: InfectedDatabase? = null
        fun getInstance(context: Context): InfectedDatabase {
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            InfectedDatabase::class.java,
                            "infected_db")
                            .fallbackToDestructiveMigration()
                            .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}