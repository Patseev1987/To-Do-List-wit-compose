package com.example.todolistwithcompose.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todolistwithcompose.data.database.tabEntity.TabItemEntity

@Database(entities = [TaskEntity::class, TabItemEntity::class], exportSchema = false, version = 5)
@TypeConverters(Convertor::class)
abstract class TasksDatabase: RoomDatabase() {

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
