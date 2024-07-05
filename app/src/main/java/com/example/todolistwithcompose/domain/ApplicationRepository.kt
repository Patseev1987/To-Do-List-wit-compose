package com.example.todolistwithcompose.domain


import kotlinx.coroutines.flow.Flow


interface ApplicationRepository {

    suspend fun insert(task: Task)

    fun getTask(): Flow<List<Task>>

    fun getTaskById(id: Long): Flow<Task?>

    suspend fun clearTaskById(id: Long)

    fun getLastId(): Long
}