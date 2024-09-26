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
import com.ssafy.ui.component.ErrorScreen
import com.ssafy.ui.component.FindButton
import com.ssafy.ui.component.LoadingScreen
import com.ssafy.ui.component.TopBar
import com.ssafy.ui.encycdetail.DescriptionSection
import com.ssafy.ui.encycdetail.ImageSection
import com.ssafy.ui.encycdetail.TitleAndDividerSection
import com.ssafy.ui.encyclopedia.EncycGrid
import com.ssafy.ui.encyclopedia.EncycGridState
import me.onebone.toolbar.CollapsingToolbar
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun ParkDetailScreenContent(
    modifier: Modifier = Modifier,
    parkDetailScreenState: ParkDetailScreenState,
    onIntent: (ParkDetailUserIntent) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopBar(
                text = if (parkDetailScreenState is ParkDetailScreenState.Loaded) parkDetailScreenState.parkName else "",
                onPop = { onIntent(ParkDetailUserIntent.OnPop) }
            )
        },
        bottomBar = {
            FindButton("모험 시작하기",
                onClick = { onIntent(ParkDetailUserIntent.OnEnterExplore) })
        }
    ) { innerPadding ->
        when (parkDetailScreenState) {
            is ParkDetailScreenState.Loading -> {
                LoadingScreen(modifier = modifier.padding(innerPadding))
            }

            is ParkDetailScreenState.Loaded -> {
                ParkDetailScreenLoaded(
                    modifier = modifier.padding(innerPadding),
                    state = parkDetailScreenState,
                    onIntent = onIntent
                )
            }

            is ParkDetailScreenState.Error -> {
                ErrorScreen(
                    modifier = modifier.padding(innerPadding),
                    parkDetailScreenState.message
                )
            }
        }

    }
}

@Composable
fun ParkDetailScreenLoaded(
    modifier: Modifier,
    state: ParkDetailScreenState.Loaded,
    onIntent: (ParkDetailUserIntent) -> Unit
) {
    val toolbarScaffoldState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        state = toolbarScaffoldState,
        scrollStrategy = ScrollStrategy.EnterAlways,
        modifier = modifier
            .fillMaxSize(),
        toolbar = {
            CollapsingToolbar(
                modifier = Modifier.fillMaxWidth(),
                collapsingToolbarState = toolbarScaffoldState.toolbarState,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    state.parkImage?.let { ImageSection(imageUrl = it) }
                    TitleAndDividerSection("공원 소개")
                    DescriptionSection(state.description)
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
                items = state.items,
                onItemClicked = { id -> onIntent(ParkDetailUserIntent.OnItemSelect(id)) },
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ParkDetailScreenPreview() {
    ParkDetailScreenContent(
        parkDetailScreenState = ParkDetailScreenState.Loaded(
            parkName = "동락공원",
            description = "동락공원이예요",
            items = List(20) { index ->
                EncycGridState(
                    plantName = "식물 $index",
                    plantImage = null,
                    isDiscovered = index % 2 == 0
                )
            }
        ),
        onIntent = {}
    )
}