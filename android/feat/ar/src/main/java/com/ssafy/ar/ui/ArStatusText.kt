package com.ssafy.ar.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.ar.core.TrackingFailureReason
import com.ssafy.ar.data.TrackingMessage

@Composable
fun ArStatusText(
    trackingFailureReason: TrackingFailureReason?,
    isAvailable: Boolean,
    isPlace: Boolean?
) {
    Text(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxWidth()
            .padding(top = 16.dp, start = 32.dp, end = 32.dp),
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        color = Color.White,
        style = MaterialTheme.typography.titleMedium,
        text = when {
            trackingFailureReason != TrackingFailureReason.NONE && trackingFailureReason != null ->
                TrackingMessage.fromTrackingFailureReason(trackingFailureReason).message

            else -> {
                if (isAvailable && isPlace != null && !isPlace)
                    TrackingMessage.SEARCH_AROUND.message
                else
                    TrackingMessage.EMPTY.message
            }
        }
    )
}