package com.ssafy.main.encyclopedia

import androidx.lifecycle.ViewModel
import com.ssafy.ui.encyclopedia.EncycScreenState
import com.ssafy.ui.encyclopedia.EncycUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class EncycScreenViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow<EncycScreenState>(EncycScreenState.Loading)
    val state: StateFlow<EncycScreenState> = _state

    fun onIntent(intent: EncycUserIntent) {

    }
}