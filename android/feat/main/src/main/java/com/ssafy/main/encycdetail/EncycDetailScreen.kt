package com.ssafy.main.encycdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssafy.ui.encycdetail.EncycDetailScreenContent
import com.ssafy.ui.encycdetail.EncycDetailUserIntent

@Composable
fun EncycDetailScreen(
    viewModel: EncycDetailScreenVIewModel = hiltViewModel(),
    onPop: () -> Unit,
    onTTSClicked: (String) -> Unit,
    onTTSShutDown: () -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    EncycDetailScreenContent(encycDetailState = uiState, onIntent = { intent ->
        when (intent) {
            is EncycDetailUserIntent.OnPop -> onPop()
            is EncycDetailUserIntent.OnTTSClicked -> onTTSClicked(intent.text)
            is EncycDetailUserIntent.OnTTSShutDown -> onTTSShutDown()
            else -> viewModel.onIntent(intent)
        }
    })
}