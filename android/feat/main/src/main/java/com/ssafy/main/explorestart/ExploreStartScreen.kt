package com.ssafy.main.explorestart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssafy.ui.explorestart.ExploreStartScreenContent
import com.ssafy.ui.explorestart.ExploreStartUserIntent

@Composable
fun ExploreStartScreen(
    parkId: Long,
    viewModel: ExploreStartScreenViewModel = hiltViewModel(),
    onExitExplore: () -> Unit,
    onEnterEncyc: () -> Unit,
    onEnterAR: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    
    LaunchedEffect(parkId) {
        viewModel.loadInitialData(parkId)
    }

    ExploreStartScreenContent(exploreStartScreenState = uiState, onIntent = {intent->
        when (intent) {
            is ExploreStartUserIntent.OnExitExploreConfirmed -> onExitExplore()
            is ExploreStartUserIntent.OnEnterEncyc -> onEnterEncyc()
            is ExploreStartUserIntent.OnCameraClicked -> onEnterAR()
            else -> viewModel.onIntent(intent)
        }
    })
}