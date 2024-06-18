package com.example.todolistwithcompose.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.todolistwithcompose.domain.TaskGroup
import com.example.todolistwithcompose.domain.TaskStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Long = 0,
    @ColumnInfo("counter")
    var title: String,
    @ColumnInfo("content")
    var content: String,
    @ColumnInfo("date")
    var date: Date,
    @ColumnInfo("taskGroup")
    val taskGroup: TaskGroup,
    @ColumnInfo("status")
    var status: TaskStatus,
    @ColumnInfo("create_date")
    val createDate: Date
)