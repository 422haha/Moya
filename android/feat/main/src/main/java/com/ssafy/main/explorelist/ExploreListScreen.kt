package com.ssafy.main.explorelist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.ui.explorelist.ExploreListScreenContent
import com.ssafy.ui.explorelist.ExploreListUserIntent

@Composable
fun ExploreListScreen(
    viewModel: ExploreListScreenViewModel = hiltViewModel(),
    onExploreItemClick: (Long) -> Unit,
    onPop: () -> Unit,
) {
    val uiState by viewModel.state.collectAsState()

    ExploreListScreenContent(exploreListScreenState = uiState, onIntent = { intent ->
        when (intent) {
            is ExploreListUserIntent.OnItemSelect -> onExploreItemClick(intent.id)
            is ExploreListUserIntent.OnPop -> onPop()
            else -> viewModel.onIntent(intent)
        }
    })
}
