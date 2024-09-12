package com.ssafy.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ssafy.ui.component.FindButton
import com.ssafy.ui.component.TopBar
import com.ssafy.ui.theme.PrimaryColor

@Composable
fun ParkDetailScreen(
    onItemClicked: (Int) -> Unit = {},
    onPop: () -> Unit = {},
    onEnterExplore: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopBar(text = "동락공원", PrimaryColor, onPop)
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
                        TitleAndDividerSection("공원 소개")
                        val text = "동락공원이예요"
                        DescriptionSection(text)
                    }
                }

                TitleAndDividerSection("관찰 가능한 동식물")
                EncycGrid(
                    items = List(8) { "능소화" },
                    modifier = Modifier.weight(1f),
                    onItemClicked = onItemClicked
                )  // 그리드 항목
            }
        },
        bottomBar = {
            FindButton("모험 시작하기", onEnterExplore)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ParkDetailScreenPreview() {
    ParkDetailScreen()
}