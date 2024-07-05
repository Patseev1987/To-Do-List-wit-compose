package com.example.todolistwithcompose

import android.app.Application
import com.example.todolistwithcompose.di.DaggerApplicationComponent


class ToDoApplication: Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}