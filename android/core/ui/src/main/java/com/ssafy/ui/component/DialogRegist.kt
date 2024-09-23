package com.ssafy.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.ui.R
import com.ssafy.ui.theme.PrimaryColor
import com.ssafy.ui.theme.SurfaceColor
import com.ssafy.ui.theme.customTypography

//인식되면 나올 다이얼로그
@Composable
fun DialogRegist(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    image: Int?
) {
    Surface(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = SurfaceColor,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(12.dp)
        ) {
            image?.let {
                Image(
                    painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "",
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End,
                modifier = Modifier.matchParentSize()
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "닫기",
                    tint = Color.Red,
                    modifier = Modifier.clickable { onDismiss() }
                )


                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "도감에서 확인하기",
                        color = PrimaryColor,
                        style = customTypography.displaySmall,
                        modifier = Modifier.clickable { onConfirm() }
                    )

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "도감으로 이동",
                        tint = PrimaryColor,
                        modifier = Modifier.clickable { onConfirm() }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DialogRegistPreview() {
    DialogRegist(onDismiss = {}, onConfirm = {}, image = R.drawable.ic_launcher_background)
}