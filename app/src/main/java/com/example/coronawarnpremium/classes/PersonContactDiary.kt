package com.example.coronawarnpremium.classes

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Keep
@Entity(tableName = "contact_diary_db")
data class PersonContactDiary (
    @PrimaryKey(autoGenerate = false)
    val PersondId: String,
    @ColumnInfo(name = "UserId")
    val UserId: String,
    @ColumnInfo(name = "Username")
    var Name: String,
    @ColumnInfo(name = "EMail")
    var EMail: String,
    @ColumnInfo(name = "Location")
    var Location: String,
    @ColumnInfo(name="Infected")
    var Infected: Boolean,
    @ColumnInfo(name="Merged")
    var Merged: Boolean,
    @ColumnInfo(name = "EncounterDate")
    var EncounterDate: LocalDate,
    @ColumnInfo(name="EncounterTime")
    var EncounterTime: String,
    @ColumnInfo(name = "DateString")
    var DateString: String
)

fun List<PersonContactDiary>.sortByNameAndIdASC(): List<PersonContactDiary> =
    this.sortedWith(compareBy { it.Name })