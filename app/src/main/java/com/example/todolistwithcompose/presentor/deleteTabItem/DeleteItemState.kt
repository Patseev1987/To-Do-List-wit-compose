package com.example.todolistwithcompose.presentor.deleteTabItem

import android.app.LauncherActivity
import com.example.todolistwithcompose.domain.TabItem

sealed class DeleteItemState {
    data object Loading : DeleteItemState()
    data class Result(
        val items: List<TabItem>,
        var isProblemWithTasks:Boolean = false
    ) : DeleteItemState()
}