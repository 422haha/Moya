package com.ssafy.main.explorelist

import androidx.lifecycle.ViewModel
import com.ssafy.ui.explorelist.ExploreListScreenState
import com.ssafy.ui.explorelist.ExploreListUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ExploreListScreenViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<ExploreListScreenState>(ExploreListScreenState.Loading)
    val state: StateFlow<ExploreListScreenState> = _state

    fun onIntent(intent: ExploreListUserIntent) {

    }
}