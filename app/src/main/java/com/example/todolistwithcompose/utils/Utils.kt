package com.example.todolistwithcompose.utils

import com.example.todolistwithcompose.data.database.TaskEntity
import com.example.todolistwithcompose.domain.Task
import java.time.Instant
import java.util.*

fun Task.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        title = this.title,
        content = this.content,
        date = this.date,
        status = this.status,
        taskGroup = this.taskGroup,
        createDate = Date.from(Instant.now())
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
    )
}