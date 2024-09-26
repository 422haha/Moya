package com.ssafy.main.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssafy.ui.home.HomeScreenContent
import com.ssafy.ui.home.HomeUserIntent

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onNavigateToParkList: () -> Unit,
    onNavigateToParkDetail: (id: Long) -> Unit,
    onNavigateToEncyc: (id: Long) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    HomeScreenContent(
        homeScreenState = uiState,
        onIntent = { intent ->
            when (intent) {
                is HomeUserIntent.OnNavigateToParkList -> onNavigateToParkList()
                is HomeUserIntent.OnSelectPopularPark -> onNavigateToParkDetail(intent.id)
                is HomeUserIntent.OnSelectClosePark -> onNavigateToParkDetail(intent.id)
                is HomeUserIntent.OnSelectEncyc -> onNavigateToEncyc(intent.id)
                else -> viewModel.onIntent(intent)
            }
        },
    )
}
