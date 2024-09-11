package com.example.uiexample.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.uiexample.R
import com.example.uiexample.ui.theme.LightBackgroundColor
import com.example.uiexample.ui.theme.PrimaryColor
import com.example.uiexample.ui.theme.SecondaryColor
import com.example.uiexample.ui.theme.SecondarySurfaceColor
import com.example.uiexample.ui.theme.SurfaceColor

@Composable
fun HomeScreen() {
    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(8.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                UserInfo()
                HomeBox(text = "나의 모험", SecondaryColor, SecondarySurfaceColor)
                HomeBox(text = "모험을 떠나요", PrimaryColor, SurfaceColor)
            }
        }
    )
}


@Composable
fun UserInfo() {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(8.dp)) {
            Image(
                painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "유저 이름",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "유저이름", modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}

@Composable
fun HomeBox(text: String, outBoxColor: Color, inBoxColor: Color) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        color = outBoxColor
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .padding(start = 12.dp)
                        .align(alignment = Alignment.CenterVertically),
                    color = LightBackgroundColor
                )
                IconButton(
                    onClick = { /* TODO */ },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Go",
                        tint = LightBackgroundColor
                    )
                }
            }

            BoxWithImage(color = inBoxColor, textColor = outBoxColor)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

@Preview(showBackground = true)
@Composable
fun BoxWithImagePreview() {
    BoxWithImage(borderWidth = 2.dp, color = SecondarySurfaceColor, textColor = SecondaryColor)
}

@Preview(showBackground = true)
@Composable
fun HomeBoxPreview() {
    HomeBox(text = "헬로", SecondaryColor, SecondarySurfaceColor)
}