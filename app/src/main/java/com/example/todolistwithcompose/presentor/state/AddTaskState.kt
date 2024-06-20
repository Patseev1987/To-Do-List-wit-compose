package com.example.todolistwithcompose.presentor.state

import com.example.todolistwithcompose.domain.Task

sealed class AddTaskState {
    data object InitState : AddTaskState()
    data class Result(val task: Task,var errorTitle:Boolean = false, var errorContext:Boolean = false) : AddTaskState()
}