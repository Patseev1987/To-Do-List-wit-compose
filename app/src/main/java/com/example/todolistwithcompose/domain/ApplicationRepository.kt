package com.example.todolistwithcompose.domain

import kotlinx.coroutines.flow.Flow


interface ApplicationRepository {


    suspend fun insertTask(task: Task)

    fun getTasks(): Flow<List<Task>>

    fun getTaskById(id: Long): Task?

    suspend fun clearTaskById(id: Long)

    fun getLastId(): Long

    fun getTaskWithFilter(filter:String): Flow<List<Task>>

    suspend fun getTabItemByName(name: String): TabItem

    suspend fun getSelectedTabItem(isSelected: Boolean = true): TabItem

    fun getTabItems():Flow <List<TabItem>>

    suspend fun clearTabItemByName(name: String)

    suspend fun insertTabItem(tabItem: TabItem)

}