package com.example.coronawarnpremium.storage.user

import androidx.room.*
import com.example.coronawarnpremium.classes.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)
    @Update
    suspend fun update(user: User)
    @Query("SELECT * FROM user_db WHERE id = :key")
    suspend fun getUser(key: String):User?
    @Query("SELECT * FROM user_db")
    suspend fun getAllUsers():List<User?>
    @Delete
    suspend fun delete(user: User)
    @Query("DELETE FROM user_db")
    suspend fun clear()
    @Query("SELECT * FROM user_db WHERE EMail = :email")
    suspend fun getUserByEmail(email: String) : User?
}