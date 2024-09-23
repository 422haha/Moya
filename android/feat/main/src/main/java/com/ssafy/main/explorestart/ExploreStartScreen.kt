package com.ssafy.main.explorestart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ssafy.ui.explorestart.ExploreStartScreenContent
import com.ssafy.ui.explorestart.ExploreStartUserIntent

@Composable
fun ExploreStartScreen(
    viewModel: ExploreStartScreenViewModel = viewModel(),
    onExitExplore: () -> Unit,
    onEnterEncyc: () -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    ExploreStartScreenContent(exploreStartScreenState = uiState, onIntent = {intent->
        when (intent) {
            is ExploreStartUserIntent.OnExitExploreConfirmed -> onExitExplore()
            is ExploreStartUserIntent.OnEnterEncyc -> onEnterEncyc()
            else -> viewModel.onIntent(intent)
        }
    })
}