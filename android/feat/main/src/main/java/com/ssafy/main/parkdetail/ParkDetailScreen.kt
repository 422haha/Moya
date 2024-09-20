package com.ssafy.main.parkdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ssafy.ui.parkdetail.ParkDetailScreenContent
import com.ssafy.ui.parkdetail.ParkDetailUserIntent

@Composable
fun ParkDetailScreen(
    viewModel: ParkDetailViewModel = viewModel(),
    onNavigateToEncycDetail: (Long) -> Unit,
    onPop: () -> Unit,
    onEnterExplore: () -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    ParkDetailScreenContent(parkDetailScreenState = uiState, onIntent = { intent ->
        when (intent) {
            is ParkDetailUserIntent.OnItemSelect -> onNavigateToEncycDetail(intent.id)
            is ParkDetailUserIntent.OnPop -> onPop()
            is ParkDetailUserIntent.OnEnterExplore -> onEnterExplore()
            else -> viewModel.onIntent(intent)
        }
    })
}