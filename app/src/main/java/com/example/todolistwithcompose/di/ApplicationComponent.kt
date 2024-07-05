package com.example.todolistwithcompose.di

import android.app.Application
import com.example.todolistwithcompose.MainActivity
import dagger.BindsInstance
import dagger.Component
import dagger.Component.Factory
import dagger.Subcomponent

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelsModule::class])
interface ApplicationComponent {

 fun  inject (main : MainActivity)

 fun getSubComponentFactory(): ComponentWithId.SubComponentFactory

    @Factory
    interface ComponentFactory {
        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}