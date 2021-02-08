package com.example.coronawarnpremium.storage.contactdiary

import android.content.Context
import android.util.Log
import androidx.room.*
import com.example.coronawarnpremium.classes.PersonContactDiary
import java.time.LocalDate
import java.time.LocalDateTime

private const val TAG = "ContactDiaryClient"
class ContactDiaryDatabaseClient(context: Context) {
    private var db: ContactDiaryDatabase = Room.databaseBuilder(context, ContactDiaryDatabase::class.java, "contact_diary_db").build()
    private var contactDiaryDao: ContactDiaryDao = db.contactDiaryDao


    suspend fun insert(person: PersonContactDiary){
        if(contactDiaryDao.getContact(person.UserId) == null){
            try {
                Log.v(TAG, "Attempting to save person")
                contactDiaryDao.insert(person)
            } catch(e: Exception){
                throw e
            }
        }
        else {
            try {
                Log.v(TAG, "Updating person...")
                contactDiaryDao.update(person)
            } catch(e: Exception){
                throw e
            }
        }
    }

    suspend fun update(person: PersonContactDiary){
        try {
            Log.v(TAG, "Updating person...")
            contactDiaryDao.update(person)
        } catch (e: Exception){
            throw e
        }
    }

    suspend fun getContact(key: String): PersonContactDiary{
        try {
            Log.v(TAG, "Attempting to save person...")
            return contactDiaryDao.getContact(key)
        } catch(e: Exception){
            throw e
        }
    }

    suspend fun getAllContacts(): ArrayList<PersonContactDiary>{
        try {
            Log.v(TAG, "Getting all persons...")
            return ArrayList(contactDiaryDao.getAllContacts())
        } catch(e: Exception){
            throw e
        }
    }

    suspend fun checkContactDate(key: String, date: LocalDate) : PersonContactDiary{
        try {
            Log.v(TAG, "Checking encounter date...")
            return contactDiaryDao.checkEncounterDate(key, date)
        } catch(e: Exception){
            throw e
        }
    }

    suspend fun destroy(){
        db.close()
    }
}