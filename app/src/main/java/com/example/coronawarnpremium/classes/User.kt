package com.example.coronawarnpremium.classes

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Keep
@Entity(tableName = "user_db")
data class User (
    @PrimaryKey(autoGenerate = false)
    val UserId: String = "",
    @ColumnInfo(name = "Username")
    var Username: String = "",
    @ColumnInfo(name = "EMail")
    var EMail: String = "",
    @ColumnInfo(name = "IMEI")
    var IMEI: String = "",
    @ColumnInfo(name ="PasswordHash")
    var PasswordHash: String = "",
    @ColumnInfo(name= "Created")
    var Created: String
    )