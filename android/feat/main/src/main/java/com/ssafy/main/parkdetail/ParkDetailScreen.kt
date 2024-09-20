package com.ssafy.main.parkdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ssafy.ui.parkdetail.ParkDetailScreenContent
import com.ssafy.ui.parkdetail.ParkDetailScreenUserIntent

@Composable
fun ParkDetailScreen(
    viewModel: ParkDetailViewModel = viewModel(),
    onNavigateToEncycDetail: (Long) -> Unit,
    onPop: () -> Unit,
    onEnterExplore: () -> Unit
) {
    val uiState by viewModel.state.collectAsState()

    ParkDetailScreenContent(parkDetailScreenState = uiState, onIntent = { intent ->
        when (intent) {
            is ParkDetailScreenUserIntent.OnItemSelect -> onNavigateToEncycDetail(intent.id)
            is ParkDetailScreenUserIntent.OnPop -> onPop()
            is ParkDetailScreenUserIntent.OnEnterExplore -> onEnterExplore()
        }
    })
}