package com.example.todolistwithcompose.presentor.mainScreen

import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.domain.Task

sealed class TabState {
    data object Init : TabState()
    data class Result(val tasks:List<Task>,
                      val tabs: List<TabItem>
                      ): TabState()
}