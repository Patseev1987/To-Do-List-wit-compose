package com.example.todolistwithcompose.presentor.state

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesomeMotion
import androidx.compose.material.icons.outlined.AutoAwesomeMotion
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.domain.Task

sealed class TabState {
    data object Init : TabState()
    data class Result(val task:List<Task>,
                      val tabs: List<TabItem>
                      ): TabState()
}