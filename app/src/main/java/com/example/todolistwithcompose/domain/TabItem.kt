package com.example.todolistwithcompose.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.todolistwithcompose.navigation.Screen

sealed class TabItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tabId:Int
) {
    data object WorkTabItem : TabItem(
        title = "Work tasks",
        selectedIcon = Icons.Filled.Work,
        unselectedIcon = Icons.Outlined.WorkOutline,
        tabId = 1
    )

    data object FamilyTabItem : TabItem(
        title = "Family tasks",
        selectedIcon = Icons.Filled.People,
        unselectedIcon = Icons.Outlined.PeopleOutline,
        tabId = 3
    )

    data object HomeTabItem : TabItem(
        title = "Home task",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        tabId = 2
    )

    data object AllTabItem : TabItem(
        title = "All task",
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




