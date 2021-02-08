package com.example.coronawarnpremium.storage.contact

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.coronawarnpremium.classes.Contact

private const val TAG = "ContactClient"
class ContactDatabaseClient(context: Context) {
    private var db: ContactDatabase = Room.databaseBuilder(context, ContactDatabase::class.java, "contact_db").build()
    private var contactDao: ContactDao = db.contactDao


    suspend fun insert(contact: Contact){
        if(contactDao.getContact(contact.id) == null){
            try {
                Log.v(TAG, "Attempting to save contact")
                contactDao.insert(contact)
            } catch(e: Exception){
                throw e
            }
        }
        else {
            try {
                Log.v(TAG, "Updating contact...")
                contactDao.update(contact)
            } catch(e: Exception){
                throw e
            }
        }
    }

    suspend fun update(contact: Contact){
        try {
            Log.v(TAG, "Updating contact...")
            contactDao.update(contact)
        } catch (e: Exception){
            throw e
        }
    }

    suspend fun getContact(key: String): Contact{
        try {
            Log.v(TAG, "Attempting to retrieve contact...")
            return contactDao.getContact(key)
        } catch(e: Exception){
            throw e
        }
    }

    suspend fun getAllContacts(): ArrayList<Contact>{
        try {
            Log.v(TAG, "Getting all contact...")
            return ArrayList(contactDao.getAllContacts())
        } catch(e: Exception){
            throw e
        }
    }

    suspend fun deleteContact(contact: Contact){
        try{
            Log.v(TAG, "Deleting contact from database...")
            contactDao.delete(contact)
        } catch( e: Exception){
            throw e
        }
    }
    suspend fun destroy(){
        db.close()
    }
}