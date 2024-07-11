package com.example.todolistwithcompose.data

import com.example.todolistwithcompose.data.database.Dao
import com.example.todolistwithcompose.domain.ApplicationRepository
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.utils.toTask
import com.example.todolistwithcompose.utils.toTaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ApplicationRepositoryImpl @Inject constructor (private val dao:Dao) : ApplicationRepository {
    override suspend fun insert(task: Task) {
        dao.insertTask(task.toTaskEntity())
    }

    override fun getTask(): Flow<List<Task>> {
        return dao.getTask().map{ tasks -> tasks.map{it.toTask()} }
    }

    override fun getTaskById(id: Long): Flow<Task?> {
        return dao.getTaskById(id).map{ task -> task?.toTask()}
    }

    override suspend fun clearTaskById(id: Long) {
        dao.clearTaskById(id)
    }

    override fun getLastId(): Long {
       return dao.getLastId()
    }
}