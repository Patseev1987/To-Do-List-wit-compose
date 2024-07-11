package com.example.todolistwithcompose.presentor.mainScreen

import com.example.todolistwithcompose.domain.Task

sealed class MainScreenState {
    data object Initial : MainScreenState()
    data object Loading : MainScreenState()
    data object Error : MainScreenState()
    data class Result(val tasks:List<Task>): MainScreenState()
}