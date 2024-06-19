package com.example.todolistwithcompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.todolistwithcompose.presentor.myUi.AddTask
import com.example.todolistwithcompose.presentor.myUi.MainScreen
import com.example.todolistwithcompose.presentor.myUi.StartScreen
import com.example.todolistwithcompose.presentor.theme.ui.ToDoListWithComposeTheme
import com.example.todolistwithcompose.presentor.viewModel.ViewModelMainScreen

class MainActivity : ComponentActivity() {
    private val viewmodel by lazy {
        ViewModelProvider(this)[ViewModelMainScreen::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListWithComposeTheme {
              //  StartScreen(viewmodel)
                AddTask()
     }
            }
        }

}
