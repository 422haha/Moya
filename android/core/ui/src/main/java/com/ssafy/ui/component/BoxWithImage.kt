package com.ssafy.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ssafy.ui.R
import com.ssafy.ui.formatDistance
import com.ssafy.ui.theme.LightBackgroundColor
import com.ssafy.ui.theme.PrimaryColor
import com.ssafy.ui.theme.SurfaceColor

@Immutable
data class BoxWithImageState(
    val info: String,
    val title: String,
    val image: String?,
)

//TODO 추후에 이미지를 받도록 수정하고 이미지가 널이면 텍스트와 버튼이 중앙에 보이도록 수정
@Composable
fun BoxWithImage(
    borderWidth: Dp = 0.dp,
    color: Color,
    textColor: Color,
    onClick: () -> Unit = {},
    icon: Int,
    state: BoxWithImageState,
) {
    Surface(
        color = color,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(BorderStroke(borderWidth, textColor), shape = RoundedCornerShape(12.dp)),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(color)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = state.image,
                    contentDescription = "이미지",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        //TODO 나중에 높이 값 수정 필요할듯??
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    placeholder = painterResource(id = R.drawable.ic_launcher_background)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(4.dp)
                        .padding(horizontal = 4.dp)
                ) {
                    Icon(
                        painterResource(id = icon),
                        contentDescription = "핀마커",
                        modifier = Modifier.size(32.dp),
                        tint = LightBackgroundColor
                    )
                    Text(
                        text = formatDistance(state.info),
                        color = Color.White,
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .padding(start = 8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = state.title,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                color = textColor
            )
            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BoxWithImagePreview() {
    BoxWithImage(
        color = SurfaceColor,
        textColor = PrimaryColor,
        icon = R.drawable.baseline_location_on_24,
        state = BoxWithImageState(
            title = "동락공원",
            info = "500m",
            image = "https://cdn.autotribune.co.kr/news/photo/202404/16048_73647_5214.png"
        ),
    )
}