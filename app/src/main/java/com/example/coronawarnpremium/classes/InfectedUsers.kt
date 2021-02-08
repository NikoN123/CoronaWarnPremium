package com.example.coronawarnpremium.classes

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "infected_db")
data class InfectedUsers (
        @PrimaryKey(autoGenerate = false)
        var UserId: String = ""
)