package com.example.coronawarnpremium.classes

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "contact_db")
data class Contact (
        @PrimaryKey(autoGenerate = false)
        var UserId: String = "",
        @ColumnInfo(name = "Username")
        var Username: String = "",
        @ColumnInfo(name = "EMail")
        var EMail: String = ""
)