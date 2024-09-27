package com.ssafy.ui.explorelist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.ui.R
import com.ssafy.ui.component.BoxWithImage
import com.ssafy.ui.component.BoxWithImageState
import com.ssafy.ui.component.DateOrLocation
import com.ssafy.ui.component.ErrorScreen
import com.ssafy.ui.component.LoadingScreen
import com.ssafy.ui.component.TopBar
import com.ssafy.ui.theme.LightBackgroundColor
import com.ssafy.ui.theme.SecondaryColor
import com.ssafy.ui.theme.SecondarySurfaceColor

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
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                TopBar(
                    text = "나의 모험",
                    onPop = { onIntent(ExploreListUserIntent.OnPop) })
                IconButton(
                    onClick = { /* TODO 검색 기능 */ },
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
            when (exploreListScreenState) {
                is ExploreListScreenState.Loading -> {
                    LoadingScreen(modifier = modifier.padding(paddingValues))
                }

                is ExploreListScreenState.Loaded -> {
                    ExploreListScreenLoaded(
                        modifier = modifier.padding(paddingValues),
                        exploreListScreenState = exploreListScreenState,
                        onIntent = onIntent
                    )
                }

                is ExploreListScreenState.Error -> {
                    ErrorScreen(
                        modifier = modifier.padding(paddingValues),
                        exploreListScreenState.message
                    )
                }
            }
        }
    )
}

@Composable
fun ExploreListScreenLoaded(
    modifier: Modifier,
    exploreListScreenState: ExploreListScreenState.Loaded,
    onIntent: (ExploreListUserIntent) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(exploreListScreenState.list) { item ->
            DateOrLocation(
                SecondarySurfaceColor,
                SecondaryColor
            ) // 날짜에 따라 보여줄 데이터 처리
            Spacer(modifier = Modifier.height(12.dp))
            BoxWithImage(
                borderWidth = 4.dp,
                color = SecondarySurfaceColor,
                textColor = SecondaryColor,
                icon = R.drawable.baseline_calendar_month_24,
                onClick = { onIntent(ExploreListUserIntent.OnItemSelect(item.id)) },
                state = item.state
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreListScreenPreview() {
    ExploreListScreenContent(
        exploreListScreenState = ExploreListScreenState.Loaded(
            list = listOf(
                BoxWithImageStateWithData(
                    id = 1,
                    state = BoxWithImageState(
                        title = "동락공원",
                        info = "2024.09.17",
                        image = null
                    )
                )
            )
        )
    )
}