package com.ssafy.main.parkdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.network.ApiResponse
import com.ssafy.network.repository.ParkRepository
import com.ssafy.ui.component.EncycCardState
import com.ssafy.ui.parkdetail.ParkDetailScreenState
import com.ssafy.ui.parkdetail.ParkDetailUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParkDetailViewModel @Inject constructor(
    private val parkRepository: ParkRepository,
): ViewModel() {
    private val _state = MutableStateFlow<ParkDetailScreenState>(ParkDetailScreenState.Loading)
    val state: StateFlow<ParkDetailScreenState> = _state

    fun loadInitialData(parkId: Long) {
        viewModelScope.launch {
            parkRepository.getPark(parkId).collectLatest { response ->
                _state.value = when (response) {
                    is ApiResponse.Success -> {
                        response.body?.let { body ->
                            ParkDetailScreenState.Loaded(
                                parkName = body.parkName,
                                description = body.description,
                                parkImage = body.imageUrl,
                                items = body.species.map {
                                    EncycCardState(
                                        id = it.speciesId,
                                        name = it.speciesName,
                                        imageUrl = it.imageUrl,
                                        isDiscovered = true
                                    )
                                }
                            )
                        } ?: ParkDetailScreenState.Error("Failed to load initial data")
                    }
                    is ApiResponse.Error -> {
                        ParkDetailScreenState.Error(response.errorMessage ?: "")
                    }
                }
            }
        }
    }

    fun onIntent(intent: ParkDetailUserIntent) {

    }
}