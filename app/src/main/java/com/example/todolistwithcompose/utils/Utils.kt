package com.example.todolistwithcompose.utils

import androidx.compose.ui.graphics.Color
import com.example.todolistwithcompose.data.database.TaskEntity
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.domain.TaskStatus
import com.example.todolistwithcompose.presentor.theme.ui.CompletedColor
import com.example.todolistwithcompose.presentor.theme.ui.InProgressColor
import com.example.todolistwithcompose.presentor.theme.ui.NotStartedColor
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

fun Task.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        title = this.title,
        content = this.content,
        date = this.date,
        status = this.status,
        taskGroup = this.taskGroup,
        createDate = LocalDateTime.now(),
        isRemind = this.isRemind
    )
}

fun TaskEntity.toTask(): Task {
    return Task(
        id = this.id,
        title = this.title,
        content = this.content,
        date = this.date,
        status = this.status,
        taskGroup = this.taskGroup,
        isRemind = this.isRemind
    )
}

fun Task.getColor():Color = when(this.status){
    TaskStatus.IN_PROGRESS -> InProgressColor
    TaskStatus.NOT_STARTED -> NotStartedColor
    TaskStatus.COMPLETED -> CompletedColor
}