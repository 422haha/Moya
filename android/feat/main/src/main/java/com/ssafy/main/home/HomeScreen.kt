package com.ssafy.main.home

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationServices
import com.ssafy.main.util.MultiplePermissionHandler
import com.ssafy.ui.home.HomeScreenContent
import com.ssafy.ui.home.HomeScreenState
import com.ssafy.ui.home.HomeUserIntent

@SuppressLint("MissingPermission")
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onNavigateToParkList: () -> Unit,
    onNavigateToParkDetail: (id: Long) -> Unit,
    onNavigateToEncyc: (id: Long) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    MultiplePermissionHandler(
        permissions =
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
    ) { result ->
        if (result.all { it.value }) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (uiState is HomeScreenState.Loading) {
                    viewModel.loadInitialData(
                        location.latitude,
                        location.longitude,
                    )
                }
            }
        }
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
