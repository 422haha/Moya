package com.ssafy.main.encycdetail

import androidx.lifecycle.ViewModel
import com.ssafy.ui.encycdetail.EncycDetailScreenState
import com.ssafy.ui.encycdetail.EncycDetailUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class EncycDetailScreenVIewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<EncycDetailScreenState>(EncycDetailScreenState.Loading)
    val state: StateFlow<EncycDetailScreenState> = _state

    fun onIntent(intent: EncycDetailUserIntent) {

    }
}