package com.ssafy.ui.parkdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ssafy.ui.component.FindButton
import com.ssafy.ui.component.TopBar
import com.ssafy.ui.encyclopedia.EncycGrid
import com.ssafy.ui.screen.DescriptionSection
import com.ssafy.ui.screen.ImageSection
import com.ssafy.ui.screen.TitleAndDividerSection
import com.ssafy.ui.theme.PrimaryColor
import me.onebone.toolbar.CollapsingToolbar
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun ParkDetailScreen(
    onItemClicked: (Long) -> Unit = {},
    onPop: () -> Unit = {},
    onEnterExplore: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopBar(
                text = "동락공원",
                backgroundColor = PrimaryColor,
                onPop = onPop
            )
        },
        bottomBar = {
            FindButton("모험 시작하기", onEnterExplore)
        }
    ) { innerPadding ->
        val toolbarScaffoldState = rememberCollapsingToolbarScaffoldState()

        CollapsingToolbarScaffold(
            state = toolbarScaffoldState,
            scrollStrategy = ScrollStrategy.EnterAlways,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            toolbar = {
                CollapsingToolbar(
                    modifier = Modifier.fillMaxWidth(),
                    collapsingToolbarState = toolbarScaffoldState.toolbarState,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        ImageSection()
                        TitleAndDividerSection("공원 소개")
                        DescriptionSection("동락공원이예요")
                    }
                }
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                TitleAndDividerSection("관찰 가능한 동식물")
                EncycGrid(
                    items = List(20) { "능소화" },
                    onItemClicked = onItemClicked,
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ParkDetailScreenPreview() {
    ParkDetailScreen()
}