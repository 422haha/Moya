package com.example.uiexample.ui

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uiexample.ui.theme.LightBackgroundColor
import com.example.uiexample.ui.theme.SecondaryColor
import com.example.uiexample.ui.theme.SecondarySurfaceColor

//TODO 이거랑 똑같이 공원 목록 만들면 될듯?
@Composable
fun ExploreListScreen() {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                TopBar(text = "나의 모험", SecondaryColor)
                IconButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = LightBackgroundColor
                    )
                }
            }
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
            ) {
                items(4) {
                    DateOrLocation(SecondarySurfaceColor, SecondaryColor) // 날짜에 따라 보여줄 데이터 처리
                    Spacer(modifier = Modifier.height(12.dp))
                    BoxWithImage(
                        borderWidth = 4.dp,
                        color = SecondarySurfaceColor,
                        textColor = SecondaryColor
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ExploreListScreenPreview() {
    ExploreListScreen()
}