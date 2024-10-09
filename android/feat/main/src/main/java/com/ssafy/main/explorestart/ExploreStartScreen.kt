package com.ssafy.main.explorestart

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationServices
import com.ssafy.main.dialog.ChallengeDialog
import com.ssafy.main.util.MultiplePermissionHandler
import com.ssafy.ui.component.ExploreDialog
import com.ssafy.ui.explorestart.ExploreStartDialogState
import com.ssafy.ui.explorestart.ExploreStartScreenContent
import com.ssafy.ui.explorestart.ExploreStartScreenState
import com.ssafy.ui.explorestart.ExploreStartUserIntent

@SuppressLint("MissingPermission")
@Composable
fun ExploreStartScreen(
    parkId: Long,
    viewModel: ExploreStartScreenViewModel = hiltViewModel(),
    onExitExplore: () -> Unit,
    onEnterEncyc: (parkId: Long) -> Unit,
    onEnterAR: (explorationId: Long) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    MultiplePermissionHandler(
        permissions =
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACTIVITY_RECOGNITION,
            ),
    ) { result ->
        if (result.all { it.value }) {
            fusedLocationClient
        }
    }

    LaunchedEffect(parkId) {
        viewModel.loadData(parkId)
        viewModel.enableSensor(context)
    }

    BackHandler {
        if (uiState is ExploreStartScreenState.Loaded) {
            viewModel.onIntent(ExploreStartUserIntent.OnExitClicked)
        } else {
            onExitExplore()
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is ExploreStartScreenState.Exit) {
            onExitExplore()
        }
    }

    if (uiState is ExploreStartScreenState.Loaded) {
        when (dialogState) {
            is ExploreStartDialogState.Exit -> {
                Dialog(onDismissRequest = { viewModel.onIntent(ExploreStartUserIntent.OnDialogDismissed) }) {
                    ExploreDialog(
                        title = "탐험을 끝마칠까요?",
                        onConfirm = {
                            viewModel.onIntent(
                                ExploreStartUserIntent.OnExitExplorationConfirmed(),
                            )
                        },
                        onDismiss = { viewModel.onIntent(ExploreStartUserIntent.OnDialogDismissed) },
                    )
                }
            }

            is ExploreStartDialogState.Challenge -> {
                Dialog(onDismissRequest = { viewModel.onIntent(ExploreStartUserIntent.OnDialogDismissed) }) {
                    ChallengeDialog(
                        explorationId = (uiState as ExploreStartScreenState.Loaded).explorationId,
                        onConfirm = { _ ->
                            onEnterAR((uiState as ExploreStartScreenState.Loaded).explorationId)
                        },
                        onDismiss = { viewModel.onIntent(ExploreStartUserIntent.OnDialogDismissed) },
                    )
                }
            }

            else -> Unit
        }
    }

    ExploreStartScreenContent(exploreStartScreenState = uiState, onIntent = { intent ->
        when (intent) {
            is ExploreStartUserIntent.OnEnterEncyc -> onEnterEncyc(parkId)
            is ExploreStartUserIntent.OnCameraClicked -> {
                if (uiState is ExploreStartScreenState.Loaded) {
                    onEnterAR((uiState as ExploreStartScreenState.Loaded).explorationId)
                }
            }

            else -> viewModel.onIntent(intent)
        }
    })
}
