package com.ssafy.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.ui.R
import com.ssafy.ui.theme.PrimaryColor
import com.ssafy.ui.theme.SurfaceColor
import com.ssafy.ui.theme.customTypography

@Composable
fun ExploreDialog(
    title: String,
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
    image: Painter? = null,
    hint: String? = null,
) {
    Surface(
        modifier =
            Modifier
                .wrapContentHeight()
                .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        color = SurfaceColor,
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = title, color = PrimaryColor, style = customTypography.displayLarge)
            Spacer(modifier = Modifier.height(40.dp))

            image?.let {
                Image(
                    painter = it,
                    contentDescription = "",
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            hint?.let {
                Text(text = it)
                Spacer(modifier = Modifier.height(16.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                CustomOutlinedButton(
                    text = "아니오",
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(16.dp))
                CustomFilledButton(
                    text = "네",
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreDialogPreview() {
    ExploreDialog("탐험을 끝마칠까요?")
}

@Preview(showBackground = true)
@Composable
fun ExploreDialogWithImagePreview() {
    ExploreDialog(
        "찾으러 가볼까요?",
        image = painterResource(id = R.drawable.ic_launcher_background),
        hint = "나는 빨간색 꽃에 가시를 갖고 있어요",
    )
}
