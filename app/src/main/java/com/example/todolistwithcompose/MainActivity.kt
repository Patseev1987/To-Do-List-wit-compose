package com.example.todolistwithcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolistwithcompose.presentor.theme.ui.MainScreen
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
     MainScreen(viewModel = viewmodel)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ToDoListWithComposeTheme {
        Greeting("Android")
    }
}