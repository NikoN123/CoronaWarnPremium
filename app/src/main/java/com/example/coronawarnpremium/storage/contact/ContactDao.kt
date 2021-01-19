package com.example.coronawarnpremium.storage.contact

import androidx.room.*
import com.example.coronawarnpremium.classes.Contact

@Dao
interface ContactDao {
    @Insert
    suspend fun insert(contact: Contact)
    @Update
    suspend fun update(contact: Contact)
    @Query("SELECT * FROM contact_db WHERE UserId = :key")
    suspend fun getContact(key: String): Contact
    @Query("SELECT * FROM contact_db")
    suspend fun getAllContacts(): List<Contact>
    @Delete
    suspend fun delete(contact: Contact)
}