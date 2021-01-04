package com.example.coronawarnpremium.classes

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

@Keep
@Entity(tableName = "contact_db")
data class PersonContactDiary (
    @PrimaryKey(autoGenerate = false)
    val PersonId: String,
    @ColumnInfo(name = "Username")
    var Name: String,
    @ColumnInfo(name = "IMEI")
    var IMEI: String,
    @ColumnInfo(name = "EMail")
    var EMail: String,
    @ColumnInfo(name = "Location")
    var Location: String,
    @ColumnInfo(name = "EncounterStart")
    var EncounterStart: LocalDateTime,
    @ColumnInfo(name = "EncounterEnd")
    var EncounterEnd: LocalDateTime
)

fun List<PersonContactDiary>.sortByNameAndIdASC(): List<PersonContactDiary> =
    this.sortedWith(compareBy { it.Name })