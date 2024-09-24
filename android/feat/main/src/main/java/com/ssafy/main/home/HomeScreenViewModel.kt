package com.ssafy.main.home

import androidx.lifecycle.ViewModel
import com.ssafy.ui.component.BoxWithImageState
import com.ssafy.ui.home.HomeScreenState
import com.ssafy.ui.home.HomeUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val state: StateFlow<HomeScreenState> = _state

    fun load() {
        _state.value = HomeScreenState.Loaded(
            userName = "사용자 이름",
            userImage = null,
            parkState = BoxWithImageState(
                info = "500m",
                title = "동락공원",
                image = "https://cdn.autotribune.co.kr/news/photo/202404/16048_73647_5214.png"
            ),
            exploreState = BoxWithImageState(
                info = "2024.09.17",
                title = "동락공원",
                image = "https://cdn.autotribune.co.kr/news/photo/202404/16048_73647_5214.png"
            ),
        )
    }

    fun onIntent(intent: HomeUserIntent) {

    }
}