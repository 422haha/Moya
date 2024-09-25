package com.ssafy.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ssafy.ui.R

@Composable
fun PopularParks(
    modifier: Modifier = Modifier,
    state: List<ParkDescriptionState> = emptyList(),
    onSelected: (parkId: Long) -> Unit = {},
) {
}

@Composable
fun ParkDescriptionCard(
    modifier: Modifier = Modifier,
    state: ParkDescriptionState,
) {
    Column(
        modifier = modifier.size(height = 240.dp, width = 208.dp),
    ) {
        AsyncImage(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            model = state.imageUrl,
            contentDescription = "공원 이미지",
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = state.name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = state.description,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
        )
    }
}

@Immutable
data class ParkDescriptionState(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
)

@Preview
@Composable
fun PopularParksPreview() {
    PopularParks()
}

@Preview(showBackground = true)
@Composable
fun ParkDescriptionCardPreview() {
    ParkDescriptionCard(
        state =
            ParkDescriptionState(
                id = 1,
                name = "동락공원",
                description = "동락공원은 동락동에 위치한 공원입니다.",
                imageUrl = "",
            ),
    )
}
