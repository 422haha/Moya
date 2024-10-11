package com.ssafy.ui.parkdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.ui.component.BackButton
import com.ssafy.ui.component.EncycCardState
import com.ssafy.ui.component.ErrorScreen
import com.ssafy.ui.component.FindButton
import com.ssafy.ui.component.LoadingScreen
import com.ssafy.ui.encycdetail.DescriptionSection
import com.ssafy.ui.encycdetail.ImageSection
import com.ssafy.ui.encycdetail.TitleAndDividerSection
import com.ssafy.ui.encyclopedia.EncycGrid
import com.ssafy.ui.theme.customTypography
import me.onebone.toolbar.CollapsingToolbar
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun ParkDetailScreenContent(
    modifier: Modifier = Modifier,
    parkDetailScreenState: ParkDetailScreenState,
    onIntent: (ParkDetailUserIntent) -> Unit = {},
) {
    Scaffold(
        topBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                parkDetailScreenState.let { state ->
                    if (state is ParkDetailScreenState.Loaded && state.parkImage != null) {
                        ImageSection(imageUrl = state.parkImage)
                    } else {
                        ImageSection(imageUrl = "")
                    }
                }
                BackButton(
                    modifier = Modifier.padding(16.dp),
                    onClick = { onIntent(ParkDetailUserIntent.OnPop) },
                )
            }
        },
        bottomBar = {
            FindButton(
                "모험 시작하기",
                onClick = { onIntent(ParkDetailUserIntent.OnEnterExplore) },
            )
        },
    ) { innerPadding ->
        when (parkDetailScreenState) {
            is ParkDetailScreenState.Loading -> {
                LoadingScreen(modifier = modifier.padding(innerPadding))
            }

            is ParkDetailScreenState.Loaded -> {
                ParkDetailScreenLoaded(
                    modifier = modifier.padding(innerPadding),
                    state = parkDetailScreenState,
                    onIntent = onIntent,
                )
            }

            is ParkDetailScreenState.Error -> {
                ErrorScreen(
                    modifier = modifier.padding(innerPadding),
                    parkDetailScreenState.message,
                )
            }
        }
    }
}

@Composable
fun ParkDetailScreenLoaded(
    modifier: Modifier,
    state: ParkDetailScreenState.Loaded,
    onIntent: (ParkDetailUserIntent) -> Unit,
) {
    val toolbarScaffoldState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        state = toolbarScaffoldState,
        scrollStrategy = ScrollStrategy.EnterAlways,
        modifier =
            modifier
                .fillMaxSize(),
        toolbar = {
            CollapsingToolbar(
                modifier = Modifier.fillMaxWidth(),
                collapsingToolbarState = toolbarScaffoldState.toolbarState,
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                ) {
                    Text(
                        text = state.parkName,
                        modifier =
                            Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = 8.dp),
                        style = customTypography.titleLarge,
                    )
                    TitleAndDividerSection("공원 소개")
                    DescriptionSection(state.description)
                }
            }
        },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize(),
        ) {
            TitleAndDividerSection("관찰 가능한 동식물")
            EncycGrid(
                items = state.items,
                onItemClicked = { id -> onIntent(ParkDetailUserIntent.OnItemSelect(id)) },
                modifier = Modifier.fillMaxHeight(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ParkDetailScreenPreview() {
    ParkDetailScreenContent(
        parkDetailScreenState =
            ParkDetailScreenState.Loaded(
                parkName = "동락공원",
                description = "동락공원이예요",
                items =
                    List(20) { index ->
                        EncycCardState(
                            id = index.toLong(),
                            name = "식물 $index",
                            imageUrl = null,
                            isDiscovered = index % 2 == 0,
                        )
                    },
            ),
        onIntent = {},
    )
}
