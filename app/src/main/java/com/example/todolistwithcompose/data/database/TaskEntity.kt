package com.example.todolistwithcompose.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todolistwithcompose.domain.TaskGroup
import com.example.todolistwithcompose.domain.TaskStatus
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long = 0L,
    @ColumnInfo("counter")
    var title: String,
    @ColumnInfo("content")
    var content: String,
    @ColumnInfo("date")
    var date: LocalDateTime?,
    @ColumnInfo("tab_item_name")
    val tabItemName: String?,
    @ColumnInfo("status")
    var status: TaskStatus,
    @ColumnInfo("create_date")
    val createDate: LocalDateTime,
    @ColumnInfo("is_remind")
    var isRemind: Boolean
)