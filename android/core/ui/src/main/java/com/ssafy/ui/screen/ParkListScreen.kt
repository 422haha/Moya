package com.example.uiexample.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uiexample.R
import com.example.uiexample.ui.theme.PrimaryColor
import com.example.uiexample.ui.theme.SurfaceColor

@Composable
fun ParkListScreen() {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                TopBar(text = "공워", PrimaryColor)
                IconButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
            ) {
                DateOrLocation(SurfaceColor, PrimaryColor)
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(4) {
                        Spacer(modifier = Modifier.height(12.dp))
                        BoxWithImage(
                            borderWidth = 4.dp,
                            color = SurfaceColor,
                            textColor = PrimaryColor
                        )
                    }
                }
            }
        }
    )
}

//TODO 여기에 color랑 이미지리소스 값 줘서 보여주면 될 듯
@Composable
fun DateOrLocation(color: Color, textColor: Color) {
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
            onClick = { /*TODO*/ },
            modifier = Modifier.align(alignment = Alignment.CenterEnd)
        ) {
            Icon(
                painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "동기화 모양"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeDetailScreenPreview() {
    ParkListScreen()
}