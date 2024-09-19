package com.ssafy.main.home

import androidx.lifecycle.ViewModel
import com.ssafy.ui.home.HomeScreenState
import com.ssafy.ui.home.HomeUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(HomeScreenState.Loading)
    val state: StateFlow<HomeScreenState> = _state

    fun onIntent(intent: HomeUserIntent) {

    }
}