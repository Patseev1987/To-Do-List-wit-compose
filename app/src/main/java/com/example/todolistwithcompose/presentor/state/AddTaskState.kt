package com.example.todolistwithcompose.presentor.state

import com.example.todolistwithcompose.domain.Task

sealed class AddTaskState {
    data object Error : AddTaskState()
    data class Result(val task: Task) : AddTaskState()
}