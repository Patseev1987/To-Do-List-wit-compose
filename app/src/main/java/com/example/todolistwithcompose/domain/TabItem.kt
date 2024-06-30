package com.example.todolistwithcompose.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class TabItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    var isSelected: Boolean = false
) {
    data object WorkTabItem : TabItem(
        title = "Work tasks",
        selectedIcon = Icons.Filled.Work,
        unselectedIcon = Icons.Outlined.WorkOutline,
        isSelected = false
    )

    data object FamilyTabItem : TabItem(
        title = "Family tasks",
        selectedIcon = Icons.Filled.People,
        unselectedIcon = Icons.Outlined.PeopleOutline,
        isSelected = false
    )

    data object HomeTabItem : TabItem(
        title = "Work task",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        isSelected = false
    )

    data object AllTabItem : TabItem(
        title = "All task",
        selectedIcon = Icons.Filled.AutoAwesomeMotion,
        unselectedIcon = Icons.Outlined.AutoAwesomeMotion,
        isSelected = false
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




