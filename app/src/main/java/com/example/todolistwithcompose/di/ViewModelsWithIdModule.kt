package com.example.todolistwithcompose.di

import androidx.lifecycle.ViewModel
import com.example.todolistwithcompose.presentor.addAndUpdateTask.AddAndUpdateTaskViewModel
import com.example.todolistwithcompose.presentor.showTask.ShowTaskViewModel
import com.example.todolistwithcompose.presentor.mainScreen.TabViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
interface ViewModelsWithIdModule {

    @Binds
    @ViewModelKey(value = AddAndUpdateTaskViewModel::class)
    @IntoMap
    fun bindAddAndUpdateViewModel(addAndUpdateViewModel: AddAndUpdateTaskViewModel): ViewModel

    @Binds
    @ViewModelKey(value = ShowTaskViewModel::class)
    @IntoMap
    fun bindShowViewModel(showTaskViewModel: ShowTaskViewModel): ViewModel


}