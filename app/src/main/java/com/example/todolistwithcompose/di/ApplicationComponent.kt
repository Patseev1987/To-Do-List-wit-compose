package com.example.todolistwithcompose.di

import android.app.Application
import com.example.todolistwithcompose.MainActivity
import com.example.todolistwithcompose.presentor.ViewModelFactory
import dagger.BindsInstance
import dagger.Component
import dagger.Component.Factory

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelsModule::class])
interface ApplicationComponent {

    fun getViewModelFactory(): ViewModelFactory

    fun getSubComponentFactory(): ComponentWithId.SubComponentFactory

    @Factory
    interface ComponentFactory {
        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}