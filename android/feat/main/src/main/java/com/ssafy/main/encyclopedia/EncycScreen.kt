package com.ssafy.main.encyclopedia

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ssafy.ui.encyclopedia.EncycScreenContent
import com.ssafy.ui.encyclopedia.EncycUserIntent

@Composable
fun EncycScreen(
    viewModel: EncycScreenViewModel = viewModel(),
    onNavigateToEncycDetail: (Long) -> Unit,
    onPop: () -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    EncycScreenContent(encycScreenState = uiState, onIntent = { intent ->
        when (intent) {
            is EncycUserIntent.OnItemSelect -> onNavigateToEncycDetail(intent.id)
            is EncycUserIntent.OnPop -> onPop()
            else -> viewModel.onIntent(intent)
        }
    })
}