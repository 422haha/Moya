package com.ssafy.main.parklist

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
import com.ssafy.ui.parklist.ParkListScreenContent
import com.ssafy.ui.parklist.ParkListScreenState
import com.ssafy.ui.parklist.ParkListUserIntent

@SuppressLint("MissingPermission")
@Composable
fun ParkListScreen(
    viewModel: ParkListScreenViewModel = hiltViewModel(),
    onParkItemClick: (Long) -> Unit,
    onPop: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    MultiplePermissionHandler(
        permissions =
            listOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
    ) { result ->
        if (result.all { it.value }) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (uiState is ParkListScreenState.Loading) {
                    viewModel.loadInitialData(
                        location.latitude,
                        location.longitude,
                    )
                }
            }
        }
    }

    ParkListScreenContent(state = uiState, onIntent = { intent ->
        when (intent) {
            is ParkListUserIntent.OnItemSelect -> onParkItemClick(intent.id)
            is ParkListUserIntent.OnPop -> onPop()
            else -> viewModel.onIntent(intent)
        }
    })
}
