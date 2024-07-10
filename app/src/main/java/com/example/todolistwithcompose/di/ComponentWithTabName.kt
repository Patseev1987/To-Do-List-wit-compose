package com.example.todolistwithcompose.di

import com.example.todolistwithcompose.presentor.viewModel.ViewModelFactory
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelsWithTabNameModule::class])
interface ComponentWithTabName {

    fun getViewModelFactory(): ViewModelFactory

    @Subcomponent.Factory
    interface SubComponentFactoryWithTabName{
        fun create(@BindsInstance tabItemName:String): ComponentWithTabName
    }


}