package com.example.coronawarnpremium.storage.user

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.coronawarnpremium.classes.User
import kotlin.Exception

private const val TAG = "UserDatabaseClient"
class UserDatabaseClient(context: Context) {
    private var db: UserDatabase = Room.databaseBuilder(context, UserDatabase::class.java, "user_db").build()
    private var userDao: UserDao = db.userDao

    suspend fun addUser(user: User){
        if(userDao.getUser(user.id) == null){
            Log.v(TAG, "Attempting to save user")
            try {
                userDao.insert(user)
            } catch (e: Exception){
                throw e
            }
        }
        else {
            Log.v(TAG, "Updating user...")
            try {
                userDao.update(user)
            } catch(e: Exception){
                throw e
            }
        }
    }

    suspend fun getUser(id: String):User?{
        Log.v(TAG, "Getting user...")
        try {
            return userDao.getUser(id)
        } catch(e: Exception){
            throw e
        }
    }

    suspend fun getUserByEmail(email: String):User?{
        Log.v(TAG, "Getting user...")
        try {
            return userDao.getUserByEmail(email)
        } catch(e: Exception){
            throw e
        }
    }

    suspend fun getAllUsers():User?{
        Log.v(TAG, "Getting users...")
        try {
            val users = userDao.getAllUsers()
            if(users.isEmpty()){
                
            }
            return users[0]
        } catch(e: Exception){
            throw e
        }
    }

    suspend fun updateUser(user: User){
        Log.v(TAG, "Updating user...")
        try {
            userDao.update(user)
        } catch(e: Exception){
            throw e
        }
    }
    suspend fun delete(user: User){
        Log.v(TAG, "Deleting user....")
        try {
            userDao.delete(user)
        } catch(e: Exception){
            throw e
        }
    }
    suspend fun clear(){
        Log.v(TAG, "Clearing database....")
        try {
            userDao.clear()
        } catch(e: Exception){
            throw e
        }
    }
    suspend fun destroy(){
        db.close()
    }
}