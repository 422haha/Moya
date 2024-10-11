package com.ssafy.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GradientBox(
    modifier: Modifier = Modifier,
    color: Color,
) {
    val brush = Brush.verticalGradient(listOf(Color.Transparent, color))
    Canvas(
        modifier = modifier,
        onDraw = {
            drawRect(brush)
        },
    )
}

@Preview
@Composable
fun GradientBoxPreview() {
    GradientBox(color = Color.White)
}
