package com.ssafy.main.exploredetail

import androidx.lifecycle.ViewModel
import com.ssafy.ui.exploredetail.ExploreDetailScreenState
import com.ssafy.ui.exploredetail.ExploreDetailUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ExploreDetailScreenViewModel @Inject constructor() : ViewModel() {
    private val _state =
        MutableStateFlow<ExploreDetailScreenState>(ExploreDetailScreenState.Loading)
    val state: StateFlow<ExploreDetailScreenState> = _state

    fun onIntent(intent: ExploreDetailUserIntent) {

    }
}