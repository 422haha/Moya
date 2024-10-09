package com.ssafy.ui.parklist

import androidx.compose.runtime.Immutable
import com.ssafy.model.LatLng
import com.ssafy.ui.component.ImageCardWithValueState

sealed interface ParkListScreenState {
    @Immutable
    data object Loading : ParkListScreenState

    @Immutable
    data class Loaded(
        val location: LatLng,
        val page: Int,
        val list: List<ImageCardWithValueState>,
    ) : ParkListScreenState

    @Immutable
    data class Error(
        val message: String,
    ) : ParkListScreenState
}
