package com.example.todolistwithcompose.di

import com.example.todolistwithcompose.presentor.ViewModelFactory
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelsWithLoadedModeModule::class])
interface ComponentWithLoadedMode {

    fun getViewModelFactory(): ViewModelFactory

    @Subcomponent.Factory
    interface SubComponentFactoryWithTabName{
        fun create(@BindsInstance loadedMode:Int): ComponentWithLoadedMode
    }


}