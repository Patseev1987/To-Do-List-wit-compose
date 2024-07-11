package com.example.todolistwithcompose.presentor.showTask

import com.example.todolistwithcompose.domain.Task

sealed class ShowTaskState {
    data object Loading : ShowTaskState()
    data class Result(val task: Task) : ShowTaskState()

}