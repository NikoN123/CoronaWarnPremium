package com.example.coronawarnpremium.util

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class Converters {
    companion object{
        @TypeConverter
        @JvmStatic
        fun fromLocalDateTime(value: LocalDateTime):String{
            return value.toString()
        }

        @TypeConverter
        @JvmStatic
        fun toLocalDateTime(value: String): LocalDateTime{
            return LocalDateTime.parse(value)
        }
    }
}