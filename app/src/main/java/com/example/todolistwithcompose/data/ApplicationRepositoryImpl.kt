package com.example.todolistwithcompose.data

import com.example.todolistwithcompose.data.database.Dao
import com.example.todolistwithcompose.domain.ApplicationRepository
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.presentor.addAndUpdateTask.AddAndUpdateTaskState
import com.example.todolistwithcompose.presentor.deleteTabItem.DeleteItemState
import com.example.todolistwithcompose.presentor.mainScreen.TabState
import com.example.todolistwithcompose.presentor.showTask.ShowTaskState
import com.example.todolistwithcompose.utils.toTabItem
import com.example.todolistwithcompose.utils.toTabItemEntity
import com.example.todolistwithcompose.utils.toTask
import com.example.todolistwithcompose.utils.toTaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ApplicationRepositoryImpl @Inject constructor (private val dao:Dao) : ApplicationRepository {

    override suspend fun insertTask(task: Task) {
        dao.insertTask(task.toTaskEntity())
    }

    override fun getTasks(): Flow<List<Task>> {
        return dao.getTasks().map{ tasks -> tasks.map{it.toTask()} }
    }

    override fun getTaskById(id: Long): Task? {
        return dao.getTaskById(id)?.toTask()
    }

    override suspend fun clearTaskById(id: Long) {
        dao.clearTaskById(id)
    }

    override suspend fun getLastId(): Long {
       return dao.getLastId()
    }

   override fun getTaskWithFilter(filter:String): Flow<List<Task>> {
     return  dao.getTaskWithFilter(filter)
           .map{ entities -> entities.map { it.toTask() }}
   }

    override suspend fun getTabItemByName(name: String): TabItem {
        return dao.getTabItemByName(name)?.toTabItem()
            ?: throw RuntimeException("TabItem with name $name not found")
    }

    override suspend fun getSelectedTabItem(isSelected: Boolean ): TabItem?{
        return dao.getSelectedTabItem(isSelected)?.toTabItem()
    }

    override fun getTabItems():Flow <List<TabItem>>{
        return dao.getTabItems().map{ tabItems -> tabItems.map { it.toTabItem() } }
    }

    override suspend fun clearTabItemByName(name: String){
        dao.clearTabItemByName(name)
    }

    override suspend fun insertTabItem(tabItem: TabItem){
        dao.insertTabItem(tabItem.toTabItemEntity())
    }

    override fun addAnUpdateTaskFlow(taskId: Long): Flow<AddAndUpdateTaskState> {
        TODO("Not yet implemented")
    }

    override fun showTaskFlow(taskId: Long): Flow<ShowTaskState> {
        return flow {
            val task = getTaskById(taskId) ?: throw Exception("Task not found")
            emit(ShowTaskState.Result(task))
        }
    }

    override fun deleteItemFlow(): Flow<DeleteItemState> {
        TODO("Not yet implemented")
    }

    override fun tabItemFlow(): Flow<TabState> {
        TODO("Not yet implemented")
    }
}