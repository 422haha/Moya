package com.ssafy.main.parkdetail

import androidx.lifecycle.ViewModel
import com.ssafy.ui.parkdetail.ParkDetailScreenState
import com.ssafy.ui.parkdetail.ParkDetailScreenUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ParkDetailViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow<ParkDetailScreenState>(ParkDetailScreenState.Loading)
    val state: StateFlow<ParkDetailScreenState> = _state

    fun onIntent(intent: ParkDetailScreenUserIntent) {

    }
}