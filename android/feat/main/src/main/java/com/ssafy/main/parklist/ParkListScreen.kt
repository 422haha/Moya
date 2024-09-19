package com.ssafy.main.parklist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ssafy.ui.parklist.ParkListScreenContent
import com.ssafy.ui.parklist.ParkListUserIntent

@Composable
fun ParkListScreen(
    viewModel: ParkListScreenViewModel = viewModel(),
    onParkItemClick: (Long) -> Unit,
    onPop: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    ParkListScreenContent(state = uiState, onIntent = { intent ->
        when(intent){
            is ParkListUserIntent.OnItemSelect -> onParkItemClick(intent.id)
            is ParkListUserIntent.OnPop -> onPop()
            else -> viewModel.onIntent(intent)
        }
    })
}
