package com.ssafy.ui.explorelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.ui.component.ErrorScreen
import com.ssafy.ui.component.LoadingScreen
import com.ssafy.ui.exploredetail.ExploreDetail
import com.ssafy.ui.theme.PrimaryColor
import com.ssafy.ui.theme.customTypography
import java.util.Date

@Composable
fun ExploreListScreenContent(
    modifier: Modifier = Modifier,
    exploreListScreenState: ExploreListScreenState,
    onIntent: (ExploreListUserIntent) -> Unit = {},
) {
//
//    var clickable by remember { mutableStateOf(true) }
//
//    BackHandler {
//        clickable = false
//        onIntent(ExploreListUserIntent.OnPop)
//    }

    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = "사용자이름",
                    modifier =
                        Modifier
                            .padding(vertical = 8.dp)
                            .padding(start = 8.dp)
                            .padding(end = 4.dp),
                    style = customTypography.titleSmall,
                    color = PrimaryColor,
                )
                Text(
                    text = "님의 탐험일지",
                    style = customTypography.displaySmall,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
        },
        content = { paddingValues ->
            when (exploreListScreenState) {
                is ExploreListScreenState.Loading -> {
                    LoadingScreen(modifier = modifier.padding(paddingValues))
                }

                is ExploreListScreenState.Loaded -> {
                    ExploreListScreenLoaded(
                        modifier = modifier.padding(paddingValues),
                        exploreListScreenState = exploreListScreenState,
                        onIntent = onIntent,
                    )
                }

                is ExploreListScreenState.Error -> {
                    ErrorScreen(
                        modifier = modifier.padding(paddingValues),
                        exploreListScreenState.message,
                    )
                }
            }
        },
    )
}

@Composable
fun ExploreListScreenLoaded(
    modifier: Modifier,
    exploreListScreenState: ExploreListScreenState.Loaded,
    onIntent: (ExploreListUserIntent) -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        ExplorePager(
            exploreDetails = exploreListScreenState.list,
            onIntent = onIntent,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreListScreenPreview() {
    ExploreListScreenContent(
        exploreListScreenState =
            ExploreListScreenState.Loaded(
                listOf(
                    ExploreDetail(
                        distance = 3.0f,
                        runningTime = 20,
                        stepCount = 100,
                        registerCount = 8,
                        date = Date(),
                        id = 1L,
                    ),
                    ExploreDetail(
                        distance = 0.5f,
                        runningTime = 60,
                        stepCount = 100,
                        registerCount = 15,
                        date = Date(),
                        id = 2L,
                    ),
                    ExploreDetail(
                        distance = 7.8f,
                        runningTime = 130,
                        stepCount = 7000,
                        registerCount = 18,
                        date = Date(),
                        id = 3L,
                    ),
                    ExploreDetail(
                        distance = 5.5f,
                        runningTime = 405,
                        stepCount = 3000,
                        registerCount = 1200,
                        date = Date(),
                        id = 4L,
                    ),
                ),
            ),
    )
}
