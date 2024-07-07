package com.example.todolistwithcompose.presentor.state


import com.example.todolistwithcompose.domain.Task

sealed class AddAndUpdateTaskState {
    data object Loading : AddAndUpdateTaskState()
    data object InitState : AddAndUpdateTaskState()
    data class Result(
        val task: Task,
        var errorTitle: Boolean = false,
        var errorContext: Boolean = false,
        var errorDate: Boolean = false,
        var isGranted: Boolean = true,
    ) : AddAndUpdateTaskState()
}