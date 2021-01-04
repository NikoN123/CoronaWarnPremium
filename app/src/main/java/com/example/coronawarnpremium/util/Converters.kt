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
            return "bla"
        }

        @TypeConverter
        @JvmStatic
        fun toLocalDateTime(value: String): LocalDateTime{
            val bla = LocalDateTime                             // Represent a date with time-of-day but lacking offset-from-UTC or time zone. As such, this does *not* represent a moment, is *not* a point on the timeline.
                .parse( value )      // Parse an input string in standard ISO 8601 format. Returns a `LocalDateTime` object.
                .toLocalDate()                            // Extract the date-only portion without the time-of-day. Still no time zone or offset-from-UTC. Returns a `LocalDate` object.
                .format(                                  // Generate text representing the value of that `LocalDate` object.
                    DateTimeFormatter                     // Define a pattern to use in generating text.
                        .ofLocalizedDate( FormatStyle.LONG )  // Automatically localize, specifying how long/abbreviated…
                        .withLocale( Locale.GERMAN )              // … and what human language and cultural norms to use in localizing.
                )
            return LocalDateTime.now()
        }
    }
}