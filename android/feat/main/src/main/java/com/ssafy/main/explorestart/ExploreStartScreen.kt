package com.ssafy.main.explorestart

import android.content.Context
import android.hardware.SensorManager
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssafy.main.dialog.ChallengeDialog
import com.ssafy.ui.component.ExploreDialog
import com.ssafy.ui.explorestart.ExploreStartDialogState
import com.ssafy.ui.explorestart.ExploreStartScreenContent
import com.ssafy.ui.explorestart.ExploreStartScreenState
import com.ssafy.ui.explorestart.ExploreStartUserIntent

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

    LaunchedEffect(parkId) {
        viewModel.loadData(parkId)
        viewModel.initializeStepSensor(sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager)
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
