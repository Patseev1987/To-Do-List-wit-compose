package com.example.todolistwithcompose.presentor.mainScreen

import com.example.todolistwithcompose.domain.Task

sealed class MainScreenState {
    data object Loading : MainScreenState()
    data class Result(val tasks: List<Task>) : MainScreenState()
}