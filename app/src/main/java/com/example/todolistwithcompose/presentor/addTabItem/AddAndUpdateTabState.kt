package com.example.todolistwithcompose.presentor.addTabItem

import com.example.todolistwithcompose.domain.TabItem

sealed class AddAndUpdateTabState {
    data object Loading : AddAndUpdateTabState()
    data class Result(
        val tabItem: TabItem,
        val errorMessage: String = "",
        val isEqualsName: Boolean = false
    ) : AddAndUpdateTabState()
}