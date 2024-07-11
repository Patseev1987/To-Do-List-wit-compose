package com.example.todolistwithcompose.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.todolistwithcompose.R
import com.example.todolistwithcompose.navigation.Screen

data class TabItem(
    val id:Int = 0,
    var name: String,
    var selectedIcon: ImageVector = Icons.Filled.QuestionMark,
    var unselectedIcon: ImageVector = Icons.Outlined.QuestionMark,
    var isSelected:Boolean = false
)




