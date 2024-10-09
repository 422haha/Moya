package com.ssafy.main.exploredetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssafy.ui.exploredetail.ExploreDetailScreenContent
import com.ssafy.ui.exploredetail.ExploreDetailUserIntent

@Composable
fun ExploreDetailScreen(
    viewModel: ExploreDetailScreenViewModel = hiltViewModel(),
    onEncycItemClicked: (Long) -> Unit,
    onPop: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    ExploreDetailScreenContent(exploreDetailScreenState = uiState, onIntent = { intent ->
        when (intent) {
            is ExploreDetailUserIntent.OnEncycItemClicked -> onEncycItemClicked(intent.encycId)
            is ExploreDetailUserIntent.OnPop -> onPop()
            else -> viewModel.onIntent(intent)
        }
    })
}
