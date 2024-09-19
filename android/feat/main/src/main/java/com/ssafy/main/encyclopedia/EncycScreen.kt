package com.ssafy.main.encyclopedia

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ssafy.ui.encyclopedia.EncycScreenContent
import com.ssafy.ui.encyclopedia.EncycScreenUserIntent

@Composable
fun EncycScreen(
    viewModel: EncycScreenViewModel = viewModel(),
    onNavigateToEncycDetail: (Long) -> Unit,
    onPop: () -> Unit
) {
    val uiState by viewModel.state.collectAsState()

    EncycScreenContent(encycScreenState = uiState, onIntent = { intent ->
        when (intent) {
            is EncycScreenUserIntent.OnItemSelect -> onNavigateToEncycDetail(intent.id)
            is EncycScreenUserIntent.OnPop -> onPop()
            else -> viewModel.onIntent(intent)
        }
    })
}