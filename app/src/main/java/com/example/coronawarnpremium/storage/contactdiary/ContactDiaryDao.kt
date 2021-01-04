package com.example.coronawarnpremium.storage.contactdiary

import androidx.room.*
import com.example.coronawarnpremium.classes.PersonContactDiary

@Dao
interface ContactDiaryDao {
    @Insert
    suspend fun insert(person: PersonContactDiary)
    @Update
    suspend fun update(person: PersonContactDiary)
    @Query("SELECT * FROM contact_db WHERE IMEI = :key")
    suspend fun getContact(key: String): PersonContactDiary?
    @Query("SELECT * FROM contact_db")
    suspend fun getAllContacts(): PersonContactDiary?
    @Delete
    suspend fun clear(person: PersonContactDiary)
}