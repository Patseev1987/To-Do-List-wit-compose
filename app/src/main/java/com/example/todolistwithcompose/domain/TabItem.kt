package com.example.todolistwithcompose.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.todolistwithcompose.R
import com.example.todolistwithcompose.navigation.Screen

sealed class TabItem(
    val titleId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tabId:Int
) {
    data object WorkTabItem : TabItem(
        titleId = R.string.work_tasks,
        selectedIcon = Icons.Filled.Work,
        unselectedIcon = Icons.Outlined.WorkOutline,
        tabId = 1
    )

    data object FamilyTabItem : TabItem(
        titleId = R.string.family_tasks,
        selectedIcon = Icons.Filled.People,
        unselectedIcon = Icons.Outlined.PeopleOutline,
        tabId = 3
    )

    data object HomeTabItem : TabItem(
        titleId = R.string.home_tasks,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        tabId = 2
    )

    data object AllTabItem : TabItem(
        titleId = R.string.all_task,
        selectedIcon = Icons.Filled.AutoAwesomeMotion,
        unselectedIcon = Icons.Outlined.AutoAwesomeMotion,
        tabId = 0
    )

    companion object {
        val tabs = listOf(
            TabItem.AllTabItem,
            TabItem.WorkTabItem,
            TabItem.HomeTabItem,
            TabItem.FamilyTabItem
        )
    }
}




