package com.ssafy.main.parkdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssafy.ui.parkdetail.ParkDetailScreenContent
import com.ssafy.ui.parkdetail.ParkDetailUserIntent

@Composable
fun ParkDetailScreen(
    parkId: Long,
    viewModel: ParkDetailViewModel = hiltViewModel(),
    onNavigateToEncycDetail: (Long) -> Unit,
    onPop: () -> Unit,
    onEnterExplore: (Long) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(parkId) {
        viewModel.loadInitialData(parkId)
    }

    ParkDetailScreenContent(parkDetailScreenState = uiState, onIntent = { intent ->
        when (intent) {
            is ParkDetailUserIntent.OnItemSelect -> onNavigateToEncycDetail(intent.id)
            is ParkDetailUserIntent.OnPop -> onPop()
            is ParkDetailUserIntent.OnEnterExplore -> onEnterExplore(parkId)
            else -> viewModel.onIntent(intent)
        }
    })
}
