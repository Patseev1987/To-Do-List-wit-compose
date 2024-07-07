package com.example.todolistwithcompose.data.database.tabEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tab_items")
data class TabItemEntity (
    @PrimaryKey()
    @ColumnInfo(name = "name")
    val name:String,
    @ColumnInfo(name = "icon_selected_name")
    val iconSelectedName:String,
    @ColumnInfo(name = "icon_unselected_name")
    val iconUnselectedName:String,
)
