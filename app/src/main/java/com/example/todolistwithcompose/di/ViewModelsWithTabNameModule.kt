package com.example.todolistwithcompose.di

import androidx.lifecycle.ViewModel
import com.example.todolistwithcompose.presentor.addAndUpdateTabItem.AddAndUpdateTabItemViewModel
import com.example.todolistwithcompose.presentor.mainScreen.TabViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
interface ViewModelsWithTabNameModule {

    @Binds
    @ViewModelKey(value = AddAndUpdateTabItemViewModel::class)
    @IntoMap
    fun bindTabViewModel(addAndUpdateTabItemViewModel: AddAndUpdateTabItemViewModel): ViewModel

}