package com.example.composetracing

import android.util.Log
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

    init {
        viewModelScope.launch {
            while(true) {
                _itemsList.value = _itemsList.value + listOf(_itemsList.value.max() + 1)
                delay(1000)
            }
        }
    }

    fun shuffle() {
        _itemsList.value = _itemsList.value.shuffled()
    }

    fun shuffleInt(clicked: Int) {
        Log.d("Hello", clicked.toString())
        _itemsList.value = _itemsList.value.shuffled()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiItemsList = _itemsList
        .flatMapLatest { flow { emit( it.map { msg -> UIItem(msg) } ) } }
        .flowOn(Dispatchers.IO)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            listOf(UIItem(0))
        )

}