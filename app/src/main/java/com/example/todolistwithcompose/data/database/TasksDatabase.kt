package com.example.todolistwithcompose.data.database

import android.app.Application
import android.content.Context
import androidx.room.*
import com.example.todolistwithcompose.data.database.tabEntity.TabItemEntity

@Database(entities = [TaskEntity::class, TabItemEntity::class], exportSchema = false, version = 1)
@TypeConverters(Convertor::class)
abstract class TasksDatabase() : RoomDatabase() {

    abstract val taskDao: Dao

    companion object {
        @Volatile
        private var INSTANCE: TasksDatabase? = null

        fun getInstance(context: Context): TasksDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TasksDatabase::class.java,
                        "tasks_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}
