package com.ssafy.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ErrorScreen(
    modifier: Modifier,
    message: String,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Text(modifier = Modifier.align(Alignment.Center), text = message)
    }
}