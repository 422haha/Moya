package com.example.uiexample.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uiexample.R
import com.example.uiexample.ui.theme.SecondaryColor

@Composable
fun CustomMarkerWithTriangle(
    markerIcon: Painter,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.wrapContentWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(SecondaryColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = markerIcon,
                contentDescription = "Custom Marker",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .height(10.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Canvas(modifier = Modifier.size(20.dp)) {
                val trianglePath = Path().apply {
                    moveTo(size.width / 2, size.height)
                    lineTo(0f, 0f)
                    lineTo(size.width, 0f)
                    close()
                    fillType = PathFillType.EvenOdd
                }
                drawPath(
                    path = trianglePath,
                    color = SecondaryColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomMarkerPreview() {
    val markerIcon = painterResource(id = R.drawable.ic_launcher_background)
    CustomMarkerWithTriangle(markerIcon = markerIcon)
}