package com.example.todolistwithcompose.domain

import java.time.LocalDateTime



data class Task(
    val id:Long = 0,
    var title: String,
    var content: String,
    var date: LocalDateTime?,
    val taskGroup: TaskGroup,
    var status: TaskStatus,
    var isRemind: Boolean = false,
)
