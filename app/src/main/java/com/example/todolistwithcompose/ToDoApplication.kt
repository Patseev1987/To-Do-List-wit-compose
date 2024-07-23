package com.example.todolistwithcompose

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.todolistwithcompose.di.ApplicationComponent
import com.example.todolistwithcompose.di.DaggerApplicationComponent


class ToDoApplication : Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent {
    return (LocalContext.current.applicationContext as ToDoApplication).component
}