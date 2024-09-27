package com.ssafy.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ssafy.ui.R
import com.ssafy.ui.theme.LightBackgroundColor
import com.ssafy.ui.theme.SurfaceColor

@Immutable
data class EncycCardState(
    val id: Long,
    val name: String,
    val imageUrl: String?,
    val isDiscovered: Boolean,
)

@Composable
fun EncycCard(
    state: EncycCardState,
    onClick: (id: Long) -> Unit = {},
) {
    Box(
        modifier =
            Modifier
                .padding(8.dp)
                .size(160.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable { onClick(state.id) },
    ) {
        AsyncImage(
            model = state.imageUrl,
            contentDescription = "도감 사진",
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(LightBackgroundColor),
        )

        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
            color =
                if (state.isDiscovered) {
                    SurfaceColor.copy(alpha = 0.5f)
                } else {
                    Color.Black.copy(alpha = 0.5f)
                },
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = state.name,
                    fontSize = 14.sp,
                    color = if (state.isDiscovered) Color.Black else LightBackgroundColor,
                )
            }
        }
    }
}

@Composable
fun EncycCircleCard(
    state: EncycCardState,
    onClick: (id: Long) -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick(state.id) },
    ) {
        AsyncImage(
            model = state.name,
            contentDescription = "plant",
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .size(120.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = state.name)
    }
}

@Preview(showBackground = true)
@Composable
fun PlantCardPreview() {
    EncycCard(
        EncycCardState(
            id = 1,
            name = "능소화",
            imageUrl = "",
            isDiscovered = true,
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun PlantCardWithFalsePreview() {
    EncycCard(
        EncycCardState(
            id = 1,
            name = "능소화",
            imageUrl = "",
            isDiscovered = false,
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun CirclePlantComponentPreview() {
    EncycCircleCard(
        EncycCardState(
            id = 1,
            name = "능소화",
            imageUrl = "",
            isDiscovered = false,
        ),
    )
}
