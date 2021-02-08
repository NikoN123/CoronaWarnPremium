package com.example.coronawarnpremium.storage.infected

import androidx.room.*
import com.example.coronawarnpremium.classes.InfectedUsers

@Dao
interface InfectedDao {
    @Insert
    suspend fun insert(user: InfectedUsers)
    @Query("SELECT * FROM infected_db")
    suspend fun getAllInfectedIds():List<InfectedUsers>
    @Delete
    suspend fun delete(user: InfectedUsers)
    @Query("DELETE FROM infected_db")
    suspend fun clear()
}