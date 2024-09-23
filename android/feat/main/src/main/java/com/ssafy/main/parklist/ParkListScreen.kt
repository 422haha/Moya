package com.ssafy.main.parklist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssafy.ui.parklist.ParkListScreenContent
import com.ssafy.ui.parklist.ParkListUserIntent

@Composable
fun ParkListScreen(
    viewModel: ParkListScreenViewModel = hiltViewModel(),
    onParkItemClick: (Long) -> Unit,
    onPop: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadInitialData(
            latitude = 37.5665,
            longitude = 126.9780,
        )
    }

    ParkListScreenContent(state = uiState, onIntent = { intent ->
        when(intent){
            is ParkListUserIntent.OnItemSelect -> onParkItemClick(intent.id)
            is ParkListUserIntent.OnPop -> onPop()
            else -> viewModel.onIntent(intent)
        }
    })
}
