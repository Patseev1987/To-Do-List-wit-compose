package com.example.todolistwithcompose.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {

//    @Insert
//    fun insert(task: TaskEntity): TaskEntity
//
//    @Update
//    fun update(task: TaskEntity): TaskEntity

    @Query("select * from task")
    fun getTask(): Flow<List<TaskEntity>>

    @Query("select * from task where id = :id")
    fun getTaskById(id: Long): TaskEntity?

    @Query("delete from task where id = :id")
    fun clearTaskById(id: Long)

}