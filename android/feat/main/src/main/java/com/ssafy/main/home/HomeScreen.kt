package com.ssafy.main.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ssafy.ui.home.HomeScreenContent
import com.ssafy.ui.home.HomeUserIntent

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = viewModel(),
    onNavigateToParkList: () -> Unit,
    onNavigateToExploreList: () -> Unit
){
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    HomeScreenContent(
        homeScreenState = uiState,
        onIntent = { intent ->
            when(intent){
                is HomeUserIntent.OnNavigateToParkList -> onNavigateToParkList()
                is HomeUserIntent.OnNavigateToExploreList -> onNavigateToExploreList()
                else -> viewModel.onIntent(intent)
            }
        }
    )
}