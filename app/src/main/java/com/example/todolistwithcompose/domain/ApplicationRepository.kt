package com.example.todolistwithcompose.domain

import com.example.todolistwithcompose.presentor.addAndUpdateTask.AddAndUpdateTaskState
import com.example.todolistwithcompose.presentor.deleteTabItem.DeleteItemState
import com.example.todolistwithcompose.presentor.mainScreen.TabState
import com.example.todolistwithcompose.presentor.showTask.ShowTaskState
import kotlinx.coroutines.flow.Flow


interface ApplicationRepository {

    suspend fun insertTask(task: Task)

    fun getTasks(): Flow<List<Task>>

    fun getTaskById(id: Long): Task?

    suspend fun clearTaskById(id: Long)

    suspend fun getLastId(): Long

    fun getTaskWithFilter(filter: String): Flow<List<Task>>

    suspend fun getTabItemByName(name: String): TabItem

    suspend fun getSelectedTabItem(isSelected: Boolean = true): TabItem?

    fun getTabItems(): Flow<List<TabItem>>

    suspend fun clearTabItemByName(name: String)

    suspend fun insertTabItem(tabItem: TabItem)

    fun addAnUpdateTaskFlow(taskId:Long):Flow<AddAndUpdateTaskState>

    fun showTaskFlow(taskId:Long): Flow<ShowTaskState>

    fun deleteItemFlow():Flow<DeleteItemState>

    fun tabItemFlow():Flow<TabState>

}