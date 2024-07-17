package com.example.todolistwithcompose.presentor.addTabItem

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistwithcompose.R
import com.example.todolistwithcompose.data.database.Dao
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.domain.useCases.GetTabItemsUseCase
import com.example.todolistwithcompose.domain.useCases.InsertTabItemUseCase
import com.example.todolistwithcompose.domain.useCases.InsertTaskUseCase
import com.example.todolistwithcompose.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class AddTabItemViewModel @Inject constructor(
    private val appContext: Application,
    private val insertTabItemsUseCase: InsertTabItemUseCase,
    private val getTabItemsUseCase: GetTabItemsUseCase,
) : ViewModel() {

    private var tabItem: TabItem
    private val _state: MutableStateFlow<AddAndUpdateTabState> = MutableStateFlow(AddAndUpdateTabState.Loading)
    val state = _state.asStateFlow()

    init {
            tabItem = TabItem(name = DEFAULT_NAME)
            _state.value = AddAndUpdateTabState.Result(tabItem)
        }

    fun setTabName(name: String) {
        tabItem = tabItem.copy(name = name)
        _state.value = AddAndUpdateTabState.Result(tabItem)
    }

    fun setSelectedIcon(selectedItemName: String) {
        tabItem = tabItem.copy(selectedIcon = selectedIcons.first { it.name.contains(selectedItemName) })
        _state.value = AddAndUpdateTabState.Result(tabItem)
    }

    fun setUnselectedIcon(unselectedItemName: String) {
        tabItem = tabItem.copy(unselectedIcon = unselectedIcons.first { it.name.contains(unselectedItemName) })
        _state.value = AddAndUpdateTabState.Result(tabItem)
    }

    fun saveTabItem(onButtonListener: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            if (checkTabItem(tabItem)) {
                insertTabItemsUseCase(tabItem)
                withContext(Dispatchers.Main) {
                    onButtonListener()
                }
            }
        }
    }

    fun getLabel(): String = appContext.getString(R.string.add_group)

    private suspend fun checkTabItem(tabItem: TabItem): Boolean {
        val tabs = getTabItemsUseCase().firstOrNull()
            ?: throw IllegalStateException("selected tab is null")
        return (!(tabs.contains(tabItem) and (tabItem.name.isNotBlank())))
    }

    companion object {
        private const val DEFAULT_NAME = "default_name"
    }


}