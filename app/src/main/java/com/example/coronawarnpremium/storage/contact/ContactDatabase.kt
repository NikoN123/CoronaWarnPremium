package com.example.coronawarnpremium.storage.contact

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.coronawarnpremium.classes.Contact

@Database(entities=[Contact::class],version=1,exportSchema = false)
abstract class ContactDatabase : RoomDatabase() {
    //creating an abstract instance of the database dao
    abstract val contactDao: ContactDao

    companion object {
        @Volatile
        private var INSTANCE: ContactDatabase? = null
        fun getInstance(context: Context): ContactDatabase {
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            ContactDatabase::class.java,
                            "contact_db")
                            .fallbackToDestructiveMigration()
                            .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}