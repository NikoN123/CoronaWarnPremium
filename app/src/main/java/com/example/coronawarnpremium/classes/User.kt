package com.example.coronawarnpremium.classes

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.coronawarnpremium.util.Converters
import java.time.LocalDateTime

@Keep
@Entity(tableName = "user_db")
data class User(
    @PrimaryKey(autoGenerate = false)
    val id: String = "",
    @ColumnInfo(name = "Username")
    var username: String = "",
    @ColumnInfo(name = "Name")
    var name: String = "",
    @ColumnInfo(name = "EMail")
    var eMail: String = "",
    @ColumnInfo(name ="PasswordHash")
    var PasswordHash: String = "",
    @ColumnInfo(name= "Created")
    var created: String,
    @ColumnInfo(name= "Infected")
    var infected: Boolean,
    @ColumnInfo(name= "Token")
    var token: String,
    @ColumnInfo(name = "Location")
    var location: String = "",
    @ColumnInfo(name = "DateOfInfection")
    var dateOfInfection: String? = null
    )

data class Infected(
        var id: String,
        var date: String,
        var location: String
)