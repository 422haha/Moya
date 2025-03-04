package com.ssafy.main.encycdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssafy.ui.encycdetail.EncycDetailScreenContent
import com.ssafy.ui.encycdetail.EncycDetailUserIntent

@Composable
fun EncycDetailScreen(
    itemId: Long,
    viewModel: EncycDetailScreenVIewModel = hiltViewModel(),
    onPop: () -> Unit,
    onTTSClicked: (String) -> Unit,
    onTTSShutDown: () -> Unit,
    onTTSReStart: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(itemId) {
        viewModel.loadInitialData(itemId)
        onTTSReStart()
    }

    EncycDetailScreenContent(encycDetailState = uiState, onIntent = { intent ->
        when (intent) {
            is EncycDetailUserIntent.OnPop -> onPop()
            is EncycDetailUserIntent.OnTTSClicked -> onTTSClicked(intent.text)
            is EncycDetailUserIntent.OnTTSShutDown -> onTTSShutDown()
            else -> viewModel.onIntent(intent)
        }
    })
}
