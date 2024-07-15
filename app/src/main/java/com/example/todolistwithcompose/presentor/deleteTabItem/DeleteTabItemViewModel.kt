package com.example.todolistwithcompose.presentor.deleteTabItem

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistwithcompose.R
import com.example.todolistwithcompose.data.database.Dao
import com.example.todolistwithcompose.domain.TabItem
import com.example.todolistwithcompose.presentor.mainScreen.TabViewModel
import com.example.todolistwithcompose.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DeleteTabItemViewModel @Inject constructor(
    private val appContext: Application,
    private val dao: Dao
) : ViewModel() {

    private val _state: MutableStateFlow<DeleteItemState> = MutableStateFlow(DeleteItemState.Loading)
    val state = _state.asStateFlow()

    init{
        viewModelScope.launch( Dispatchers.IO) {
            var items = dao
                .getTabItems()
                .map { entities ->
                    entities.map {
                        entity -> entity.toTabItem()
                    }.filterNot { item -> item.name == TabViewModel.ALL_TASKS.name }
                }
                .firstOrNull()
                ?: throw IllegalArgumentException ("Items must exist!")
            _state.value = DeleteItemState.Result(items = items)
        }
    }



}