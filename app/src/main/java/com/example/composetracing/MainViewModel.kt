package com.example.composetracing

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _itemsList: MutableStateFlow<List<Int>> = MutableStateFlow(listOf(0))

    private val _currentItem: MutableStateFlow<UIItem?> = MutableStateFlow(null)
    val currentItem = _currentItem.asStateFlow()

    init {
        viewModelScope.launch {
            while(_itemsList.value.size < 200) {
                _itemsList.value = _itemsList.value + listOf(_itemsList.value.max() + 1)
                delay(100)
            }
        }

        /*viewModelScope.launch {
            while(true) {
                _currentItem.value = try {
                    _itemsList.value.subList(0, 10).random().let { UIItem(it) }
                } catch (e: Exception) {
                    _itemsList.value.random().let { UIItem(it) }
                }
                delay(100)
            }
        }*/
    }

    fun shuffle() {
        _itemsList.value = _itemsList.value.shuffled()
    }

    fun shuffleMe(value: Int) {
        _itemsList.value = _itemsList.value.shuffled()
    }

    fun setCurrentItem(item: UIItem) {
        _currentItem.value = item
    }

    fun isCurrent(item: UIItem) = _currentItem.value == item

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiItemsList = _itemsList
        .flatMapLatest {
            flow {
                emit( it.map { msg -> UIItem(msg) })
            }
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

}