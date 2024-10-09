package com.ssafy.main.explorelist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.network.ApiResponse
import com.ssafy.network.repository.ExploreDiaryRepository
import com.ssafy.network.repository.UserRepository
import com.ssafy.ui.exploredetail.ExploreDetail
import com.ssafy.ui.explorelist.ExploreListScreenState
import com.ssafy.ui.explorelist.ExploreListUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ExploreListScreenViewModel
    @Inject
    constructor(
        private val exploreDiaryRepository: ExploreDiaryRepository,
        private val userRepository: UserRepository,
    ) : ViewModel() {
        private val _state = MutableStateFlow<ExploreListScreenState>(ExploreListScreenState.Loading)
        val state: StateFlow<ExploreListScreenState> = _state

        fun loadInitialData(
            page: Int,
            size: Int,
        ) {
            viewModelScope.launch {
                val userInfo = userRepository.getName()
                Log.d("TAG", "loadInitialData: $userInfo")
                exploreDiaryRepository.getExploreDiaryList(page, size).collect { response ->
                    _state.value =
                        when (response) {
                            is ApiResponse.Success -> {
                                response.body?.let { body ->
                                    ExploreListScreenState.Loaded(
                                        userName = if (userInfo is ApiResponse.Success) userInfo.body?.name ?: "" else "",
                                        list = body.data.map {
                                            val DF = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)

                                            val result = DF.parse(it.startTime)

                                            ExploreDetail(
                                                id = it.explorationId,
                                                distance = it.distance,
                                                runningTime = it.duration,
                                                questCompletedCount = it.questCompletedCount,
                                                registerCount = it.collectedCount,
                                                date = result ?: Date(),
                                                parkName = it.parkName,
                                                imageUrl = it.imageUrl,
                                            )
                                        },
                                    )
                                } ?: ExploreListScreenState.Error("Failed to load initial data")
                            }

                            is ApiResponse.Error ->
                                ExploreListScreenState.Error(
                                    response.errorMessage ?: "Unknown error",
                                )
                        }
                }
            }
        }

        fun onIntent(intent: ExploreListUserIntent) {
        }
    }
