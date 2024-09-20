package com.ssafy.main.explorestart

import androidx.lifecycle.ViewModel
import com.ssafy.ui.explorestart.ExploreStartScreenState
import com.ssafy.ui.explorestart.ExploreStartUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ExploreStartScreenViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<ExploreStartScreenState>(ExploreStartScreenState.Loading)
    val state: StateFlow<ExploreStartScreenState> = _state

    fun onIntent(intent: ExploreStartUserIntent) {

    }
}