package com.example.todolistwithcompose.presentor.state

import com.example.todolistwithcompose.domain.Task

sealed class TabState {
    data object Init : TabState()
    data class Result(val task:List<Task>): TabState()
}