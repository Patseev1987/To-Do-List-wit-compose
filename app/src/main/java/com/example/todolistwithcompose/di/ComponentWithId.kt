package com.example.todolistwithcompose.di

import com.example.todolistwithcompose.presentor.viewModel.ViewModelFactory
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelsWithIdModule::class])
interface ComponentWithId {

    fun getViewModelFactory(): ViewModelFactory

    @Subcomponent.Factory
    interface SubComponentFactory{
        fun create(@BindsInstance taskId:Long): ComponentWithId
    }


}