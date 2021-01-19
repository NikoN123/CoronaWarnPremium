package com.example.coronawarnpremium.storage.contactdiary

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.coronawarnpremium.classes.PersonContactDiary
import com.example.coronawarnpremium.storage.user.UserDao
import com.example.coronawarnpremium.storage.user.UserDatabase
import com.example.coronawarnpremium.util.Converters

@Database(entities=[PersonContactDiary::class],version=1,exportSchema = false)
@TypeConverters(Converters::class)
abstract class ContactDiaryDatabase : RoomDatabase() {
    //creating an abstract instance of the database dao
    abstract val contactDiaryDao: ContactDiaryDao

    companion object {
        @Volatile
        private var INSTANCE: ContactDiaryDatabase? = null
        fun getInstance(context: Context): ContactDiaryDatabase {
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ContactDiaryDatabase::class.java,
                        "contact_diary_db")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}