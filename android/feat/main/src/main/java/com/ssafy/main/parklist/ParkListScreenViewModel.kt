package com.ssafy.main.parklist

import androidx.lifecycle.ViewModel
import com.ssafy.ui.parklist.ParkListScreenState
import com.ssafy.ui.parklist.ParkListUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ParkListScreenViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<ParkListScreenState>(ParkListScreenState.Loading)
    val state: StateFlow<ParkListScreenState> = _state

    fun onIntent(intent: ParkListUserIntent) {

    }
}