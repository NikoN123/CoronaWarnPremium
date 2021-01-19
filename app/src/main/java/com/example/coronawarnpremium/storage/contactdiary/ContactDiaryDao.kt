package com.example.coronawarnpremium.storage.contactdiary

import androidx.room.*
import com.example.coronawarnpremium.classes.PersonContactDiary
import com.example.coronawarnpremium.util.Converters
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
@TypeConverters(Converters::class)
interface ContactDiaryDao {
    @Insert
    suspend fun insert(person: PersonContactDiary)
    @Update
    suspend fun update(person: PersonContactDiary)
    @Query("SELECT * FROM contact_diary_db WHERE UserId = :key")
    suspend fun getContact(key: String): PersonContactDiary
    @Query("SELECT * FROM contact_diary_db WHERE UserId = :key AND EncounterDate = :date")
    suspend fun checkEncounterDate(key: String, date: LocalDateTime) : PersonContactDiary
    @Query("SELECT * FROM contact_diary_db")
    suspend fun getAllContacts(): List<PersonContactDiary>
    @Delete
    suspend fun delete(person: PersonContactDiary)
}