package com.example.uiexample.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uiexample.ui.theme.PrimaryColor

@Composable
fun ParkDetailScreen() {
    Scaffold(
        topBar = {
            TopBar(text = "동락공원", PrimaryColor)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                ImageSection()
                Box {
                    Column {
                        TitleAndDividerSection()
                        DescriptionSection()
                    }
                }

                TitleAndDividerSection()
                EncycGrid(items = List(8) { "능소화" }, modifier = Modifier.weight(1f))  // 그리드 항목
            }
        },
        bottomBar = {
            FindButton("모험 시작하기")
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ParkDetailScreenPreview() {
    ParkDetailScreen()
}