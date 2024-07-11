package com.example.todolistwithcompose.presentor.addAndUpdateTabItem

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistwithcompose.R
import com.example.todolistwithcompose.data.database.Dao
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class AddAndUpdateTabItemViewModel @Inject constructor(
    private val tabItemName: String? = null,
    private val appContext: Application,
    private val dao: Dao
) : ViewModel() {

    private lateinit var tabItem: TabItem
    private val _state: MutableStateFlow<AddAndUpdateTabState> = MutableStateFlow(AddAndUpdateTabState.Loading)
    val state = _state.asStateFlow()

    init {
        if (tabItemName.isNullOrBlank()) {
            tabItem = TabItem(DEFAULT_NAME)
            _state.value = AddAndUpdateTabState.Result(tabItem)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                dao.getTabItemByName(tabItemName).collect {
                    tabItem = it?.toTabItem() ?: throw RuntimeException("Unknown tabItem")
                    _state.value = AddAndUpdateTabState.Result(tabItem)
                }
            }
        }
    }

    fun setTabName(name: String) {
        tabItem = tabItem.copy(name = name)
        _state.value = AddAndUpdateTabState.Result(tabItem)
    }

    fun setSelectedIcon(selectedItemName: String) {
        tabItem = tabItem.copy(selectedIcon = selectedIcons.first { it.name == selectedItemName })
        _state.value = AddAndUpdateTabState.Result(tabItem)
    }

    fun setUnselectedIcon(unselectedItemName: String) {
        tabItem = tabItem.copy(unselectedIcon = unselectedIcons.first { it.name == unselectedItemName })
        _state.value = AddAndUpdateTabState.Result(tabItem)
    }


    fun saveTabItem(onButtonListener: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            if (checkTabItem(tabItem)) {
                dao.insertTabItem(tabItem.toTabItemEntity())
                withContext(Dispatchers.Main) {
                    onButtonListener()
                }
            }
        }
    }

    fun getLabel(): String = if (tabItemName == null) appContext.getString(R.string.add_group)
    else appContext.getString(R.string.update_group)

    private suspend fun checkTabItem(tabItem: TabItem): Boolean {
        val tabs = dao.getTabItems().map{ entity -> entity.toTabItem() }
        return  (!(tabs.contains(tabItem) and (tabItem.name.isNotBlank())))
    }


    companion object {
        private const val DEFAULT_NAME = "default_name"
    }


}