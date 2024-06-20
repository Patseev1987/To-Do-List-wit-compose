package com.example.todolistwithcompose.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todolistwithcompose.domain.TaskGroup
import com.example.todolistwithcompose.domain.TaskStatus
import java.time.LocalDateTime
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
    var date: LocalDateTime?,
    @ColumnInfo("taskGroup")
    val taskGroup: TaskGroup,
    @ColumnInfo("status")
    var status: TaskStatus,
    @ColumnInfo("create_date")
    val createDate: LocalDateTime
)