package com.example.todolistwithcompose.data.database

import android.app.Application
import android.content.Context
import androidx.room.*

@Database(entities = [TaskEntity::class], exportSchema = false, version = 5)
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
                        "tasks"
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
