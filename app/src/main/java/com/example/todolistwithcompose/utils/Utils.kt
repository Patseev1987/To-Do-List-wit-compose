package com.example.todolistwithcompose.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.todolistwithcompose.data.database.TaskEntity
import com.example.todolistwithcompose.data.database.tabEntity.TabItemEntity
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.domain.TaskStatus
import com.example.todolistwithcompose.presentor.theme.ui.CompletedColor
import com.example.todolistwithcompose.presentor.theme.ui.InProgressColor
import com.example.todolistwithcompose.presentor.theme.ui.NotStartedColor
import com.example.todolistwithcompose.presentor.theme.ui.Orange
import java.time.LocalDateTime
import java.util.*

fun Task.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        title = this.title,
        content = this.content,
        date = this.date,
        status = this.status,
        tabItemName = this.tabItemName,
        createDate = LocalDateTime.now(),
        isRemind = this.isRemind
    )
}

fun TaskEntity.toTask(): Task {
    return Task(
        id = this.id,
        title = this.title,
        content = this.content,
        date = this.date,
        status = this.status,
        tabItemName = this.tabItemName,
        isRemind = this.isRemind
    )
}

fun Task.getColor():Color = when(this.status){
    TaskStatus.IN_PROGRESS -> InProgressColor
    TaskStatus.NOT_STARTED -> NotStartedColor
    TaskStatus.COMPLETED -> CompletedColor
}

fun Task.getBoarderWidth(): Dp {
    return when(this.getBoarderColor()){
        Color.Red ->4.dp
        Orange -> 3.dp
        Yellow -> 2.dp
        else -> 1.dp
    }
}

fun Task.getBoarderColor(): Color {
    val taskDate = this.date
    return if ( taskDate != null && this.status != TaskStatus.COMPLETED) {
        when {
            LocalDateTime.now().isAfter(taskDate) -> Color.Red
            LocalDateTime.now().isAfter(taskDate.minusDays(2L)) -> Orange
            LocalDateTime.now().isAfter(taskDate.minusDays(7L)) -> Yellow
            else -> Green
        }
    }
    else Transparent
}

fun TabItem.toTabItemEntity(): TabItemEntity {
    return TabItemEntity(
        name = this.name,
        iconSelectedName = this.selectedIcon.name,
        iconUnselectedName = this.unselectedIcon.name,
        isSelected = this.isSelected
    )
}

fun TabItemEntity.toTabItem(): TabItem {
    return TabItem(
        name = this.name,
        selectedIcon = selectedIcons.first{ icon -> icon.name == this.iconSelectedName},
        unselectedIcon = unselectedIcons.first{ icon -> icon.name == this.iconUnselectedName},
        isSelected = this.isSelected
    )
}



val selectedIcons = listOf(
    Icons.Filled.Add,
    Icons.Filled.Call,
    Icons.Filled.AcUnit,
    Icons.Filled.AccessTime,
    Icons.Filled.AccountBox,
    Icons.Filled.Accessibility,
    Icons.Filled.AccountBalance,
    Icons.Filled.AirplanemodeActive,
    Icons.Filled.AirportShuttle,
    Icons.Filled.Anchor,
    Icons.Filled.AttachFile,
    Icons.Filled.GroupAdd,
    Icons.Filled.Book,
    Icons.Filled.Build,
    Icons.Filled.Cookie,
    Icons.Filled.BusinessCenter,
    Icons.Filled.CurrencyRuble,
    Icons.Filled.Cake,
    Icons.Filled.CalendarToday,
    Icons.Filled.Coffee,
    Icons.Filled.Work,
    Icons.Filled.Dangerous,
    Icons.Filled.ChildFriendly,
    Icons.Filled.FamilyRestroom,
    Icons.Filled.Home,
    Icons.Filled.Hotel,
)

val unselectedIcons = listOf(
    Icons.Outlined.Add,
    Icons.Outlined.Call,
    Icons.Outlined.AcUnit,
    Icons.Outlined.AccessTime,
    Icons.Outlined.AccountBox,
    Icons.Outlined.Accessibility,
    Icons.Outlined.AccountBalance,
    Icons.Outlined.AirplanemodeActive,
    Icons.Outlined.AirportShuttle,
    Icons.Outlined.Anchor,
    Icons.Outlined.AttachFile,
    Icons.Outlined.GroupAdd,
    Icons.Outlined.Book,
    Icons.Outlined.Build,
    Icons.Outlined.Cookie,
    Icons.Outlined.BusinessCenter,
    Icons.Outlined.CurrencyRuble,
    Icons.Outlined.Cake,
    Icons.Outlined.CalendarToday,
    Icons.Outlined.Coffee,
    Icons.Outlined.Work,
    Icons.Outlined.Dangerous,
    Icons.Outlined.ChildFriendly,
    Icons.Outlined.FamilyRestroom,
    Icons.Outlined.Home,
    Icons.Outlined.Hotel,
)