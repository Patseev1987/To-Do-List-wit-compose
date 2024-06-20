package com.example.todolistwithcompose.data.database

import android.app.Application
import androidx.room.*

@Database(entities = [TaskEntity::class], exportSchema = false, version = 2)
@TypeConverters(Convertor::class)
abstract class TasksDatabase() : RoomDatabase() {

    abstract val taskDao: Dao

    companion object {
        @Volatile
        private var INSTANCE: TasksDatabase? = null

        fun getInstance(application: Application): TasksDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        application,
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
