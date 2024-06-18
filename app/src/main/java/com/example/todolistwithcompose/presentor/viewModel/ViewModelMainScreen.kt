package com.example.todolistwithcompose.presentor.viewModel

import android.provider.ContactsContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.todolistwithcompose.data.database.Dao
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.domain.TaskGroup
import com.example.todolistwithcompose.domain.TaskStatus
import com.example.todolistwithcompose.presentor.state.MainScreenState
import com.example.todolistwithcompose.utils.toTask
import com.example.todolistwithcompose.utils.toTaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date

class ViewModelMainScreen(
) : ViewModel() {
    private val _state = MutableStateFlow(MainScreenState.Initial)
//    val state = dao.getTask()
//        .map { it.map { taskEntity -> taskEntity.toTask() } }

    val state: MutableSharedFlow<List<Task>> = MutableSharedFlow<List<Task>>()
    var tasks:List<Task> = listOf(
        Task(
            id = 1L,
            title = "Title 1",
            content = "Content content content",
            date = Date.from(Instant.now()),
            taskGroup = TaskGroup.HOME_TASK,
            status = TaskStatus.NOT_STARTED
        ),
        Task(
            id = 2L,
            title = "Title 2",
            content = "Content content content",
            date = Date.from(Instant.now()),
            taskGroup = TaskGroup.HOME_TASK,
            status = TaskStatus.NOT_STARTED
        ),
        Task(
            id = 6L,
            title = "Title 6",
            content = "Content content content",
            date = Date.from(Instant.now()),
            taskGroup = TaskGroup.FAMILY_TASK,
            status = TaskStatus.NOT_STARTED
        ),
        Task(
            id = 5L,
            title = "Title 5",
            content = "Content content content",
            date = Date.from(Instant.now()),
            taskGroup = TaskGroup.HOME_TASK,
            status = TaskStatus.COMPLETED
        ),
        Task(
            id = 3L,
            title = "Title 3",
            content = "Content content content",
            date = Date.from(Instant.now()),
            taskGroup = TaskGroup.WORK_TASK,
            status = TaskStatus.COMPLETED
        ),
        Task(
            id = 4L,
            title = "Title 4",
            content = "Content content content",
            date = Date.from(Instant.now()),
            taskGroup = TaskGroup.FAMILY_TASK,
            status = TaskStatus.IN_PROGRESS
        ),

    )

    init {

        viewModelScope.launch {
            delay(500)
            state.emit(
                tasks.sortedBy { it.id }
            )
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            val newTasks = tasks.toMutableList()
            newTasks.remove(task)
            tasks = newTasks
            state.emit(
                newTasks.toList().sortedBy { it.taskGroup }
            )
        }
    }
//
//    fun addTask(title: String, content: String, taskGroup: TaskGroup, status: TaskStatus, date: Long) {
//        dao.insert(
//            Task(
//                title = title,
//                content = content,
//                taskGroup = taskGroup,
//                status = status,
//                date = Date(date),
//            ).toTaskEntity()
//        )
//    }
//
//    fun getTaskById(id: Long): Task {
//        return dao.getTaskById(id)?.toTask() ?: throw Exception("No task found")
//    }
//
//    fun updateTask(task: Task) {
//        dao.update(task.toTaskEntity())
//    }

}