package com.example.todolistwithcompose.data.database

import androidx.room.*
import androidx.room.Dao
import com.example.todolistwithcompose.data.database.tabEntity.TabItemEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Query("select * from tasks")
    fun getTasks(): Flow<List<TaskEntity>>

    @Query("select * from tasks where tab_item_name = :filter")
    fun getTaskWithFilter(filter:String): Flow<List<TaskEntity>>

    @Query("select * from tasks where id = :id")
    fun getTaskById(id: Long):TaskEntity?

    @Query("delete from tasks where id = :id")
    suspend fun clearTaskById(id: Long)

    @Query("select id from tasks order by id desc limit 1")
    fun getLastId(): Long

    @Query("select * from tab_items where name = :name")
    suspend fun getTabItemByName(name: String):TabItemEntity?

    @Query("select * from tab_items where is_selected = :isSelected")
    suspend fun getSelectedTabItem(isSelected: Boolean = true):TabItemEntity?

    @Query("select * from tab_items")
    fun getTabItems():Flow <List<TabItemEntity>>

    @Query("delete  from tab_items where name = :name")
    suspend fun clearTabItemByName(name: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTabItem(tabItemEntity: TabItemEntity)

}