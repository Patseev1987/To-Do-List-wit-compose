package com.example.todolistwithcompose.data.database

import androidx.room.*
import androidx.room.Dao
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Query("select * from task")
    fun getTask(): Flow<List<TaskEntity>>

    @Query("select * from task where id = :id")
    suspend fun getTaskById(id: Long): TaskEntity?

    @Query("delete from task where id = :id")
    suspend fun clearTaskById(id: Long)


}