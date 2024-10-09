package com.ssafy.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ssafy.ui.R
import com.ssafy.ui.formatDistance
import com.ssafy.ui.theme.OnPrimaryColor

@Composable
fun ImageCardWithValue(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    state: ImageCardWithValueState,
    onClick: (id: Long) -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable { onClick(state.id) },
    ) {
        AsyncImage(
            modifier =
                Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            model = state.imageUrl,
            contentDescription = "공원 이미지",
            placeholder = painterResource(id = R.drawable.park_placeholder),
        )
        Box(
            modifier = Modifier.fillMaxWidth().height(40.dp).align(Alignment.BottomCenter),
        ) {
            Surface(
                modifier =
                    Modifier.fillMaxWidth().height(40.dp).blur(
                        radius = 4.dp,
                        edgeTreatment = BlurredEdgeTreatment.Unbounded,
                    ),
                color = Color.Black.copy(alpha = 0.5f),
            ) {}
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = state.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnPrimaryColor,
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = OnPrimaryColor,
                )
                Text(
                    text = state.value.formatDistance(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnPrimaryColor,
                )
            }
        }
    }
}

@Immutable
data class ImageCardWithValueState(
    val id: Long,
    val title: String,
    val value: String,
    val imageUrl: String? = null,
)

@Preview(showBackground = true)
@Composable
fun ImageCardWithValuePreview() {
    ImageCardWithValue(
        icon = Icons.Filled.LocationOn,
        state =
            ImageCardWithValueState(
                id = 1,
                title = "동락공원",
                value = "99",
                imageUrl = "",
            ),
    )
}
