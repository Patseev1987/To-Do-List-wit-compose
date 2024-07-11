package com.example.todolistwithcompose.presentor.addAndUpdateTask

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistwithcompose.R
import com.example.todolistwithcompose.data.database.Dao
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.domain.Task
import com.example.todolistwithcompose.domain.TaskGroup
import com.example.todolistwithcompose.domain.TaskStatus
import com.example.todolistwithcompose.presentor.mainScreen.TabViewModel
import com.example.todolistwithcompose.utils.AlarmReceiver
import com.example.todolistwithcompose.utils.toTabItem
import com.example.todolistwithcompose.utils.toTask
import com.example.todolistwithcompose.utils.toTaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject


class AddAndUpdateTaskViewModel @Inject constructor(
    private val taskId: Long,
    private val appContext: Application,
    private val taskDao:Dao
) : ViewModel() {

    private lateinit var task: Task
    private lateinit var tabs:List<TabItem>
    private val _state: MutableStateFlow<AddAndUpdateTaskState> = MutableStateFlow(AddAndUpdateTaskState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO){
            tabs = taskDao.getTabItems().map{tabItemEntity -> tabItemEntity.toTabItem() }
            if (taskId != 0L) {
                    taskDao.getTaskById(taskId).collect {
                        task = it?.toTask() ?: throw IllegalArgumentException("Task not found")
                        _state.value = AddAndUpdateTaskState.Result(task =  task, tabs = tabs)
                }
            } else {
                task = DEFAULT_TASK
                _state.value = AddAndUpdateTaskState.Result(task =  task, tabs = tabs)
            }
        }


    }

    fun setTitle(title: String) {
        task = task.copy(title = title)
        _state.value = AddAndUpdateTaskState.Result(task =  task, tabs = tabs)
    }

    fun setContent(content: String) {
        task = task.copy(content = content)
        _state.value = AddAndUpdateTaskState.Result(task =  task, tabs = tabs)
    }

    fun setTabItemName(value: String) {
        val taskGroup = TaskGroup.entries.first { appContext.getString(it.idString) == value }
        task = task.copy(tabItemName = value)
        _state.value = AddAndUpdateTaskState.Result(task =  task, tabs = tabs)
    }

    fun setStatus(value: String) {
        val status = TaskStatus.entries.first { appContext.getString(it.idString) == value }
        task.status = status
        _state.value = AddAndUpdateTaskState.Result(task =  task, tabs = tabs)
    }

    fun setTime(time: LocalTime) {
        val date = task.date?.toLocalDate()
        val newDate = LocalDateTime.of(date, time)
        task = task.copy(date = newDate)
        _state.value = AddAndUpdateTaskState.Result(task =  task, tabs = tabs)
    }

    fun setDate(date: LocalDate) {
        val time = task.date?.toLocalTime()
        val newDate = LocalDateTime.of(date, time)
        task = task.copy(date = newDate)
        _state.value = AddAndUpdateTaskState.Result(task =  task, tabs = tabs)
    }
    fun setGroup(tabItem: TabItem) {
        task.tabItemName = tabItem.name
        _state.value = AddAndUpdateTaskState.Result(task =  task, tabs = tabs)
    }

    fun saveTask(onButtonListener: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            if (checkTask(task)) {
                if (task.isRemind) {
                    if (task.id != 0L) {
                        cancelAlarm(taskId)
                    }
                    if (checkDateForRemind()) {
                        setAlarm()
                    } else {
                        _state.value = AddAndUpdateTaskState.Result(
                            task = task,
                            errorDate = true,
                            tabs = tabs
                        )
                        return@launch
                    }
                }
                taskDao.insertTask(task.toTaskEntity())
                withContext(Dispatchers.Main) {
                    onButtonListener()
                }
            }
        }
    }

    private fun checkTask(task: Task): Boolean {
        return when {
            task.title.isEmpty() -> {
                _state.value = AddAndUpdateTaskState.Result(task, errorTitle = true, tabs = tabs)
                false
            }

            task.content.isEmpty() -> {
                _state.value = AddAndUpdateTaskState.Result(task, errorContext = true, tabs = tabs)
                false
            }

            else -> true
        }
    }

    fun getLabel() = if (taskId == 0L) appContext.getString(R.string.add_task)
    else appContext.getString(R.string.update_task)


    private fun setAlarm() {
        val alarmManager = appContext.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = AlarmReceiver.newAlarmIntent(appContext, task.title, task.content)
        val time = task.date?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
        val alarmTime = time
            ?: throw RuntimeException("wrong time")
        val requestCodeFromIdTask = if (taskId == 0L) taskDao.getLastId().toInt() else taskId.toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            requestCodeFromIdTask,
            intent,
            FLAG_IMMUTABLE
        )
        if (isAlarmPermissionGranted()){
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                alarmTime,
                pendingIntent,
            )
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Intent().also { myIntent ->
                    myIntent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                    appContext.startActivity(myIntent)
                }
            }
        }

    }

    private fun isAlarmPermissionGranted(): Boolean {
        val alarmManager = appContext.getSystemService<AlarmManager>()!!
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    private fun cancelAlarm(taskId: Long) {
        val alarmManager = appContext.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = AlarmReceiver.newAlarmIntent(appContext, task.title, task.content)
        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            taskId.toInt(),
            intent,
            FLAG_MUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun changeIsRemind() {
        task = task.copy(isRemind = !task.isRemind)
        _state.value = AddAndUpdateTaskState.Result(task =  task, tabs = tabs)
        task.apply {
            date = if (isRemind) getNowDateWithoutSeconds() else null
        }
    }

    private fun checkDateForRemind(): Boolean = task.date?.isAfter(LocalDateTime.now())
        ?: throw RuntimeException("wrong date")


    private fun getNowDateWithoutSeconds(): LocalDateTime {
        return LocalDateTime.of(
            LocalDateTime.now().year,
            LocalDateTime.now().month,
            LocalDateTime.now().dayOfMonth,
            LocalDateTime.now().hour,
            LocalDateTime.now().minute,
            0,
        )
    }

    fun permissionsDenied() {
        _state.value = AddAndUpdateTaskState.Result(task, isGranted = false, tabs = tabs)
    }


    private companion object{
        val DEFAULT_TASK = Task(
            title = "",
            content = "",
            date = null,
            tabItemName = TabViewModel.ALL_TASKS.name,
            status = TaskStatus.NOT_STARTED
        )
    }
}