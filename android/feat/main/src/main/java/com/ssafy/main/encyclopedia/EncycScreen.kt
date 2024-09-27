package com.ssafy.main.encyclopedia

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssafy.ui.encyclopedia.EncycScreenContent
import com.ssafy.ui.encyclopedia.EncycUserIntent

@Composable
fun EncycScreen(
    viewModel: EncycScreenViewModel = hiltViewModel(),
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