package com.example.todolistwithcompose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.todolistwithcompose.presentor.addTabItem.IconList
import com.example.todolistwithcompose.presentor.theme.ui.ToDoListWithComposeTheme
import com.example.todolistwithcompose.utils.selectedIcons
import com.example.todolistwithcompose.utils.unselectedIcons

class IconChoiceActivity : ComponentActivity() {
    private var typeIcon: Int = UNKNOWN_TYPE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            parseIntent()
            ToDoListWithComposeTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    IconList(
                        icons = when (typeIcon) {
                            FILLED_TYPE_ICON -> selectedIcons
                            OUTLINE_TYPE_ICON -> unselectedIcons
                            else -> throw IllegalArgumentException("Invalid icon choice")
                        }
                    ) { icon ->
                        saveData(icon)
                        finish()
                    }
                }
            }
        }
    }

    private fun parseIntent() {
        typeIcon = intent.getIntExtra(EXTRA_TYPE_ICON, UNKNOWN_TYPE)
    }

    private fun saveData(icon: ImageVector) {
        Intent().apply {
            putExtra(EXTRA_ICON_NAME, icon.name)
            putExtra(EXTRA_TYPE_ICON, typeIcon)
            setResult(RESULT_OK, this)
        }
    }

    companion object {
        const val EXTRA_ICON_NAME = "extra_icon_name"
        const val FILLED_TYPE_ICON = 101
        const val OUTLINE_TYPE_ICON = 102
        const val EXTRA_TYPE_ICON = "type_icon"
        private const val UNKNOWN_TYPE = -1

        fun getIntent(context: Context, typeIcon: Int) = Intent(context, IconChoiceActivity::class.java).apply {
            putExtra(EXTRA_TYPE_ICON, typeIcon)
        }

    }
}

