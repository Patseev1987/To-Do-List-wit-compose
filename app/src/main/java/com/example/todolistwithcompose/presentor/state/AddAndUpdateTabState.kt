package com.example.todolistwithcompose.presentor.state

import com.example.todolistwithcompose.domain.TabItem

sealed class AddAndUpdateTabState {
    data object Loading : AddAndUpdateTabState()
    data object Error : AddAndUpdateTabState()
    data class Result(val tabItem: TabItem) : AddAndUpdateTabState()
}