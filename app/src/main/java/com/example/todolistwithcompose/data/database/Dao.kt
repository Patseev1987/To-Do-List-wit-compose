package com.example.todolistwithcompose.data.database

import androidx.room.*
import androidx.room.Dao
import com.example.todolistwithcompose.data.database.tabEntity.TabItemEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Query("select * from tasks")
    fun getTask(): Flow<List<TaskEntity>>

    @Query("select * from tasks where id = :id")
    fun getTaskById(id: Long):Flow <TaskEntity?>

    @Query("delete from tasks where id = :id")
    suspend fun clearTaskById(id: Long)

    @Query("select id from tasks order by id desc limit 1")
    fun getLastId(): Long

    @Query("select * from tab_items where name = :name")
    fun getTabItemByName(name: String):Flow <TabItemEntity?>

    @Query("select * from tab_items")
    fun getTabItems():Flow<List<TabItemEntity>>

    @Query("delete  from tab_items where name = :name")
    suspend fun clearTabItemByName(name: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tabItemEntity: TabItemEntity)
}