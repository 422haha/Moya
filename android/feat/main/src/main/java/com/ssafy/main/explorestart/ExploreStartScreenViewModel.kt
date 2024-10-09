package com.ssafy.main.explorestart

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.skele.moya.background.di.LocationManager
import com.ssafy.network.ApiResponse
import com.ssafy.network.repository.ExplorationRepository
import com.ssafy.network.request.ExplorationEndRequestBody
import com.ssafy.ui.explorestart.ExploreMarkerState
import com.ssafy.ui.explorestart.ExploreStartDialogState
import com.ssafy.ui.explorestart.ExploreStartScreenState
import com.ssafy.ui.explorestart.ExploreStartUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreStartScreenViewModel
    @Inject
    constructor(
        private val explorationRepository: ExplorationRepository,
        private val locationManager: LocationManager,
    ) : ViewModel() {
        private val _state = MutableStateFlow<ExploreStartScreenState>(ExploreStartScreenState.Loading)
        val state: StateFlow<ExploreStartScreenState> = _state

        private val _dialogState =
            MutableStateFlow<ExploreStartDialogState>(ExploreStartDialogState.Closed)
        val dialogState: StateFlow<ExploreStartDialogState> = _dialogState

        fun loadData(parkId: Long) {
            when (val currentState = _state.value) {
                is ExploreStartScreenState.Loaded ->
                    updateData(
                        explorationId = currentState.explorationId,
                        parkId = parkId,
                    )

                else -> loadInitialData(parkId)
            }
        }

        fun onIntent(intent: ExploreStartUserIntent) {
            when (intent) {
                is ExploreStartUserIntent.OnDialogDismissed -> {
                    _dialogState.value = ExploreStartDialogState.Closed
                }

                is ExploreStartUserIntent.OnExitClicked -> {
                    _dialogState.value = ExploreStartDialogState.Exit
                }

                is ExploreStartUserIntent.OnExitExplorationConfirmed -> {
                    _dialogState.value = ExploreStartDialogState.Closed
                    endExploration()
                }

                is ExploreStartUserIntent.OnOpenChallengeList -> {
                    _dialogState.value = ExploreStartDialogState.Challenge
                }
            }
        }

        fun startTracking(context: Context) {
            if (state.value is ExploreStartScreenState.Loaded) return
            locationManager.initialize(context)
            locationManager.startTracking(context)
        }

        private lateinit var sensorManager: SensorManager
        private var stepCounterSensor: Sensor? = null
        private lateinit var sensorEventListener: SensorEventListener
        private var initialStepCount: Int? = null
        private var stepCount = 0

        fun initializeStepSensor(sensorManager: SensorManager) {
            this.sensorManager = sensorManager
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

            sensorEventListener =
                object : SensorEventListener {
                    override fun onAccuracyChanged(
                        sensor: Sensor?,
                        accuracy: Int,
                    ) {}

                    override fun onSensorChanged(event: SensorEvent) {
                        if (initialStepCount == null) {
                            initialStepCount = event.values[0].toInt()
                        } else {
                            stepCount = event.values[0].toInt()
                        }
                    }
                }

            stepCounterSensor?.let {
                sensorManager.registerListener(
                    sensorEventListener,
                    it,
                    SensorManager.SENSOR_DELAY_NORMAL,
                )
            }
        }

        fun disposeStepSensor() {
            sensorManager.unregisterListener(sensorEventListener)
        }

        private fun loadInitialData(parkId: Long) {
            viewModelScope.launch {
                explorationRepository.startExploration(parkId).collectLatest { response ->
                    _state.value =
                        when (response) {
                            is ApiResponse.Success -> {
                                response.body?.let { body ->
                                    ExploreStartScreenState.Loaded(
                                        explorationId = body.id,
                                        npcPositions =
                                            body.npcs
                                                .flatMap { it.positions }
                                                .map { LatLng(it.latitude, it.longitude) },
                                        discoveredPositions =
                                            body.myDiscoveredSpecies
                                                .map {
                                                    ExploreMarkerState(
                                                        it.name,
                                                        it.imageUrl,
                                                        it.positions,
                                                    )
                                                },
                                        speciesPositions =
                                            body.species
                                                .flatMap { it.positions }
                                                .map { LatLng(it.latitude, it.longitude) },
                                    )
                                } ?: ExploreStartScreenState.Error("Failed to load initial data")
                            }

                            is ApiResponse.Error -> {
                                ExploreStartScreenState.Error(response.errorMessage ?: "Unknown error")
                            }
                        }
                }
            }
        }

        private fun updateData(
            explorationId: Long,
            parkId: Long,
        ) {
            viewModelScope.launch {
                explorationRepository
                    .getExplorationData(parkId = parkId, explorationId = explorationId)
                    .collectLatest { response ->
                        _state.value =
                            when (response) {
                                is ApiResponse.Success -> {
                                    response.body?.let { body ->
                                        ExploreStartScreenState.Loaded(
                                            explorationId = body.id,
                                            npcPositions =
                                                body.npcs
                                                    .flatMap { it.positions }
                                                    .map { LatLng(it.latitude, it.longitude) },
                                            discoveredPositions =
                                                body.myDiscoveredSpecies
                                                    .map {
                                                        ExploreMarkerState(
                                                            it.name,
                                                            it.imageUrl,
                                                            it.positions,
                                                        )
                                                    },
                                            speciesPositions =
                                                body.species
                                                    .flatMap { it.positions }
                                                    .map { LatLng(it.latitude, it.longitude) },
                                        )
                                    } ?: ExploreStartScreenState.Error("Failed to load initial data")
                                }

                                is ApiResponse.Error -> {
                                    ExploreStartScreenState.Error(response.errorMessage ?: "Unknown error")
                                }
                            }
                    }
            }
        }

        private fun endExploration() {
            viewModelScope.launch {
                if (state.value is ExploreStartScreenState.Loaded) {
                    val uiState = state.value as ExploreStartScreenState.Loaded

                    explorationRepository
                        .endExploration(
                            uiState.explorationId,
                            body =
                                ExplorationEndRequestBody(
                                    route = locationManager.getLocationList(), // TODO : 이동경로 저장
                                    steps = 0,
                                ),
                        ).collectLatest { response ->
                            when (response) {
                                is ApiResponse.Success -> {
                                    locationManager.stopTracking()
                                    _state.value = ExploreStartScreenState.Exit
                                }

                                is ApiResponse.Error -> {
                                    // _state.value = ExploreStartScreenState.Exit
                                }
                            }
                        }
                }
            }
        }
    }
