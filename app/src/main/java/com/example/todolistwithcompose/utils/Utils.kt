package com.example.todolistwithcompose.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.todolistwithcompose.data.database.TaskEntity
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.domain.TaskStatus
import com.example.todolistwithcompose.presentor.theme.ui.CompletedColor
import com.example.todolistwithcompose.presentor.theme.ui.InProgressColor
import com.example.todolistwithcompose.presentor.theme.ui.NotStartedColor
import com.example.todolistwithcompose.presentor.theme.ui.Orange
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

fun Task.getBoarderWidth(): Dp {
    return when(this.getBoarderColor()){
        Color.Red ->4.dp
        Orange -> 3.dp
        Yellow -> 2.dp
        else -> 1.dp
    }
}

fun Task.getBoarderColor(): Color {
    val taskDate = this.date
    if ( taskDate != null && this.status != TaskStatus.COMPLETED) {
        return when {
            LocalDateTime.now().isAfter(taskDate) -> Color.Red
            LocalDateTime.now().isAfter(taskDate.minusDays(2L)) -> Orange
            LocalDateTime.now().isAfter(taskDate.minusDays(7L)) -> Yellow
            else -> Green
        }
    }
    else return Transparent
}