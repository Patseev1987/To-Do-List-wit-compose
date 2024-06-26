package com.example.todolistwithcompose.presentor.state

import com.example.todolistwithcompose.domain.Task

sealed class UpdateTaskState {
    data object Loading : UpdateTaskState()
    data class Result(val task: Task,var errorTitle:Boolean = false, var errorContext:Boolean = false) : UpdateTaskState()
}