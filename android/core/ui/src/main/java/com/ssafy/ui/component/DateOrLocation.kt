package com.ssafy.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.ui.R
import com.ssafy.ui.theme.PrimaryColor
import com.ssafy.ui.theme.SurfaceColor

@Composable
fun DateOrLocation(color: Color, textColor: Color, painter: Int? = null) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .background(color)
    ) {
        Text(
            text = "현재 내 위치예요",
            modifier = Modifier
                .align(alignment = Alignment.CenterStart)
                .padding(start = 8.dp),
            color = textColor
        )
        IconButton(
            onClick = { /*TODO 위치 동기화 */ },
            modifier = Modifier.align(alignment = Alignment.CenterEnd)
        ) {
            painter?.let { painterResource(id = it) }?.let {
                Image(
                    painter = it,
                    contentDescription = "동기화 모양",
                    modifier = Modifier.padding(4.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DateOrLocationPreview() {
    DateOrLocation(
        color = SurfaceColor,
        textColor = PrimaryColor,
        painter = R.drawable.baseline_my_location_24
    )
}
