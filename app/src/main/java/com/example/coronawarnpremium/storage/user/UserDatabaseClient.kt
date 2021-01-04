package com.example.coronawarnpremium.storage.user

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.coronawarnpremium.classes.User

private const val TAG = "UserDatabaseClient"
class UserDatabaseClient (context: Context) {
    private var db: UserDatabase = Room.databaseBuilder(context, UserDatabase::class.java, "user_db").build()
    private var userDao: UserDao = db.userDao

    suspend fun addUser(user: User){
        if(userDao.getUser(user.UserId) == null){
            Log.v(TAG, "Attempting to save user")
            userDao.insert(user)
        }
        else {
            Log.v(TAG, "Updating user...")
            userDao.update(user)
        }
    }

    suspend fun getUser(id: String):User?{
        Log.v(TAG, "Getting user...")
        return userDao.getUser(id)
    }

    suspend fun updateUser(user: User){
        Log.v(TAG, "Updating user...")
        userDao.update(user)
    }
    suspend fun clear(user: User){
        Log.v(TAG, "Clearing database....")
        userDao.clear(user)
    }
}