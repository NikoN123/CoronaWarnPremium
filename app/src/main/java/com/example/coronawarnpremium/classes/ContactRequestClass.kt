package com.example.coronawarnpremium.classes

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class ContactRequestClass (
        var id: Int,
        var requestingId: String,
        var requestedId: String,
        var requestingContact: Contact
)