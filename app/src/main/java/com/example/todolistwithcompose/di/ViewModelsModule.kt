package com.example.todolistwithcompose.di

import androidx.lifecycle.ViewModel
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

}