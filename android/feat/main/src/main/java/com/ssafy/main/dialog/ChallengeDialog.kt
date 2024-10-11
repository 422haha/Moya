package com.ssafy.main.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.ui.component.ChallengeDialogContent

@Composable
fun ChallengeDialog(
    modifier: Modifier = Modifier,
    explorationId: Long,
    viewModel: ChallengeDialogViewModel = hiltViewModel(),
    onConfirm: (id: Long) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    val uiState by viewModel.state

    LaunchedEffect(explorationId) {
        viewModel.loadInitialData(explorationId)
    }

    ChallengeDialogContent(
        missions = uiState,
        onConfirm = { id ->
            onConfirm(id)
            onDismiss()
        },
        onDismiss = onDismiss,
    )
}
