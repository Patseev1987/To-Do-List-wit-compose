package com.example.todolistwithcompose.presentor.addTabItem

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistwithcompose.R
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.domain.useCases.GetTabItemByNameUseCase
import com.example.todolistwithcompose.domain.useCases.GetTabItemsUseCase
import com.example.todolistwithcompose.domain.useCases.InsertTabItemUseCase
import com.example.todolistwithcompose.utils.selectedIcons
import com.example.todolistwithcompose.utils.unselectedIcons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class AddTabItemViewModel @Inject constructor(
    private val appContext: Application,
    private val insertTabItemsUseCase: InsertTabItemUseCase,
    private val getTabItemsUseCase: GetTabItemsUseCase,
    private val getTabItemByNameUseCase: GetTabItemByNameUseCase
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
        _state.value = AddAndUpdateTabState.Loading
        _state.value = AddAndUpdateTabState.Result(tabItem)
    }

    fun setUnselectedIcon(unselectedItemName: String) {
        tabItem = tabItem.copy(unselectedIcon = unselectedIcons.first { it.name.contains(unselectedItemName) })
        _state.value = AddAndUpdateTabState.Loading
        _state.value = AddAndUpdateTabState.Result(tabItem)
    }

    fun saveTabItem(onButtonListener: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            if ((_state.value as AddAndUpdateTabState.Result).isEqualsName) {
                val tabItemWithOldIcon = getTabItemByNameUseCase(tabItem.name)
                tabItemWithOldIcon.apply {
                    selectedIcon = tabItem.selectedIcon
                    unselectedIcon = tabItem.unselectedIcon
                }
                tabItem = tabItemWithOldIcon
            }
            insertTabItemsUseCase(tabItem)
            withContext(Dispatchers.Main) {
                onButtonListener()
            }
        }
    }

    fun getLabel(): String = appContext.getString(R.string.add_group)

    fun checkTabItem(onButtonListener: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val tabs = getTabItemsUseCase().firstOrNull()
                ?: throw IllegalStateException("selected tab is null")

            if (tabs.map { it.name }.contains(tabItem.name)) {
                _state.value = (_state.value as AddAndUpdateTabState.Result)
                    .copy(isEqualsName = true)
                return@launch
            }
            if (tabItem.name.isBlank()) {
                _state.value = (_state.value as AddAndUpdateTabState.Result)
                    .copy(errorMessage = appContext.getString(R.string.the_group_name_can_t_be_blank))
                return@launch
            }
            saveTabItem { onButtonListener() }
        }
    }

    fun resetDialog() {
        _state.value = (_state.value as AddAndUpdateTabState.Result)
            .copy(isEqualsName = false)
    }

    companion object {
        private const val DEFAULT_NAME = "default_name"
    }


}