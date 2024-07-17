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
        id = this.id,
        name = this.name,
        iconSelectedName = this.selectedIcon.name,
        iconUnselectedName = this.unselectedIcon.name,
        isSelected = this.isSelected
    )
}

fun TabItemEntity.toTabItem(): TabItem {
    return TabItem(
        id = this.id,
        name = this.name,
        selectedIcon = selectedIcons.first{ icon -> icon.name == this.iconSelectedName},
        unselectedIcon = unselectedIcons.first{ icon -> icon.name == this.iconUnselectedName},
        isSelected = this.isSelected
    )
}



val selectedIcons = listOf(
    Icons.Filled.Call,
    Icons.Filled.AddAPhoto,
    Icons.Filled.AddReaction,
    Icons.Filled.Agriculture,
    Icons.Filled.AccountBox,
    Icons.Filled.AccountBalance,
    Icons.Filled.AssignmentInd,
    Icons.Filled.AirportShuttle,
    Icons.Filled.AttachFile,
    Icons.Filled.AttachEmail,
    Icons.Filled.Attractions,
    Icons.Filled.AutoStories,
    Icons.Filled.GroupAdd,
    Icons.Filled.Book,
    Icons.Filled.BackHand,
    Icons.Filled.Build,
    Icons.Filled.Badge,
    Icons.Filled.Bathtub,
    Icons.Filled.Bed,
    Icons.Filled.BorderColor,
    Icons.Filled.BreakfastDining,
    Icons.Filled.BubbleChart,
    Icons.Filled.Cookie,
    Icons.Filled.BusinessCenter,
    Icons.Filled.Cake,
    Icons.Filled.CalendarToday,
    Icons.Filled.Coffee,
    Icons.Filled.CameraAlt,
    Icons.Filled.Castle,
    Icons.Filled.Celebration,
    Icons.Filled.Chair,
    Icons.Filled.Cloud,
    Icons.Filled.ColorLens,
    Icons.Filled.Dangerous,
    Icons.Filled.ChildFriendly,
    Icons.Filled.Home,
    Icons.Filled.Hotel,
    Icons.Filled.AutoAwesomeMotion,
    Icons.Filled.QuestionMark
)

val unselectedIcons = listOf(
    Icons.Outlined.Call,
    Icons.Outlined.AddAPhoto,
    Icons.Outlined.AddReaction,
    Icons.Outlined.AccountBox,
    Icons.Outlined.Agriculture,
    Icons.Outlined.AccountBalance,
    Icons.Outlined.AssignmentInd,
    Icons.Outlined.AirportShuttle,
    Icons.Outlined.AttachFile,
    Icons.Outlined.AttachEmail,
    Icons.Outlined.Attractions,
    Icons.Outlined.AutoStories,
    Icons.Outlined.GroupAdd,
    Icons.Outlined.Book,
    Icons.Outlined.Build,
    Icons.Outlined.BackHand,
    Icons.Outlined.Badge,
    Icons.Outlined.Bathtub,
    Icons.Outlined.Bed,
    Icons.Outlined.BorderColor,
    Icons.Outlined.BreakfastDining,
    Icons.Outlined.BubbleChart,
    Icons.Outlined.Cookie,
    Icons.Outlined.BusinessCenter,
    Icons.Outlined.Cake,
    Icons.Outlined.CalendarToday,
    Icons.Outlined.Coffee,
    Icons.Outlined.CameraAlt,
    Icons.Outlined.Castle,
    Icons.Outlined.Celebration,
    Icons.Outlined.Chair,
    Icons.Outlined.Cloud,
    Icons.Outlined.ColorLens,
    Icons.Outlined.Dangerous,
    Icons.Outlined.ChildFriendly,
    Icons.Outlined.Home,
    Icons.Outlined.Hotel,
    Icons.Outlined.AutoAwesomeMotion,
    Icons.Outlined.QuestionMark
)