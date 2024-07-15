package com.example.todolistwithcompose.di

import androidx.lifecycle.ViewModel
import com.example.todolistwithcompose.presentor.addTabItem.AddTabItemViewModel
import com.example.todolistwithcompose.presentor.deleteTabItem.DeleteTabItemViewModel
import com.example.todolistwithcompose.presentor.mainScreen.TabViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
interface ViewModelsModule {
    @Binds
    @ViewModelKey(value = TabViewModel::class)
    @IntoMap
    fun bindTabViewModel(tabViewModel: TabViewModel): ViewModel


    @Binds
    @ViewModelKey(value = AddTabItemViewModel::class)
    @IntoMap
    fun bindAddTabItemViewModel(addAndUpdateTabItemViewModel: AddTabItemViewModel): ViewModel

    @Binds
    @ViewModelKey(value = DeleteTabItemViewModel::class)
    @IntoMap
    fun bindDeleteTabItemViewModel(deleteTabItemViewModel: DeleteTabItemViewModel): ViewModel
}