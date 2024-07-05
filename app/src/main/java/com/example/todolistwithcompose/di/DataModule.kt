package com.example.todolistwithcompose.di

import android.app.Application
import com.example.todolistwithcompose.data.ApplicationRepositoryImpl
import com.example.todolistwithcompose.data.database.TasksDatabase
import com.example.todolistwithcompose.domain.ApplicationRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    fun bindApplicationRepository(appRepositoryImpl: ApplicationRepositoryImpl): ApplicationRepository


    companion object {
        @Provides
        fun provideDao(application:Application) = TasksDatabase.getInstance(application).taskDao
    }


}