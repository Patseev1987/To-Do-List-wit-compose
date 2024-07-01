package com.example.todolistwithcompose.presentor.state

import com.example.todolistwithcompose.domain.Task

sealed class TestNavigationTabState {
    data object Init : TestNavigationTabState()
    data class Result(val task:List<Task>): TestNavigationTabState()
}