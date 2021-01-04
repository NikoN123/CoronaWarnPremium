package com.example.coronawarnpremium.storage.user

import androidx.room.*
import com.example.coronawarnpremium.classes.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)
    @Update
    suspend fun update(user: User)
    @Query("SELECT * FROM user_db WHERE IMEI = :key")
    suspend fun getUser(key: String):User?
    @Delete
    suspend fun clear(user: User)
}