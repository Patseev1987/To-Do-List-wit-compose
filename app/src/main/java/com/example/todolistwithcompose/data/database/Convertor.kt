package com.example.todolistwithcompose.data.database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class Convertor {
    @TypeConverter
    fun toLocalDataTime(dateString : String?): LocalDateTime? {
        return if (dateString == null) null else LocalDateTime.parse(dateString)
    }

    @TypeConverter
    fun toString(date: LocalDateTime?): String? {
        return date?.toString()
    }
}