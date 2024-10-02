package com.ssafy.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Icon(
        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
        contentDescription = "back",
        tint = Color.Black,
        modifier =
            Modifier
                .padding(16.dp)
                .size(32.dp)
                .drawBehind {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.7f),
                    )
                }.clickable { onClick() },
    )
}

@Preview
@Composable
private fun BackButtonPreview() {
    BackButton()
}
