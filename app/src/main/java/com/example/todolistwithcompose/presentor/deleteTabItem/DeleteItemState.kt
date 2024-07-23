package com.example.todolistwithcompose.presentor.deleteTabItem

import com.example.todolistwithcompose.domain.TabItem

sealed class DeleteItemState {
    data object Loading : DeleteItemState()
    data class Result(
        val items: List<TabItem>,
        var isProblemWithTasks: Boolean = false,
        val message: String = "",
        val isError: Boolean = false
    ) : DeleteItemState()

}