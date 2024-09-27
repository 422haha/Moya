package com.ssafy.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ssafy.ui.R

@Composable
fun ImageCardWithTitleDescription(
    modifier: Modifier = Modifier,
    state: ImageCardWithTitleDescriptionState,
    onClick: (id: Long) -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .width(208.dp)
                .clickable { onClick(state.id) },
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
            text = state.title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = state.description,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Immutable
data class ImageCardWithTitleDescriptionState(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String? = null,
)

@Preview(showBackground = true)
@Composable
fun ImageCardWithTitleDescriptionPreview() {
    ImageCardWithTitleDescription(
        state =
            ImageCardWithTitleDescriptionState(
                id = 1,
                title = "동락공원",
                description = "동락공원은 동락동에 위치한 공원입니다.",
                imageUrl = "",
            ),
    )
}
