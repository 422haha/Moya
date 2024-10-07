package com.ssafy.ar.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ssafy.ar.R

@Composable
fun DialogRegist(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    image: String
) {
    Surface(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF0FFEB),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(12.dp)
        ) {
            AsyncImage(
                model = image,
                contentDescription = "",
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .size(72.dp),
                contentScale = ContentScale.Crop
            )

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
                        color = Color(0xFF32A287),
                        fontSize = 24.sp,
                        modifier = Modifier
                            .clickable { onConfirm() }
                            .padding(end = 4.dp)
                    )

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "도감으로 이동",
                        tint = Color(0xFF32A287),
                        modifier = Modifier
                            .clickable { onConfirm() }
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DialogRegistPreview() {
    DialogRegist(onDismiss = {}, onConfirm = {}, image = R.drawable.maple.toString())
}