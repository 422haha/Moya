package com.ssafy.ui.parklist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.model.LatLng
import com.ssafy.ui.component.ErrorScreen
import com.ssafy.ui.component.ImageCardWithValue
import com.ssafy.ui.component.ImageCardWithValueState
import com.ssafy.ui.component.LoadingScreen
import com.ssafy.ui.component.PaginatedLazyColumn
import com.ssafy.ui.component.TopBar

@Composable
fun ParkListScreenContent(
    modifier: Modifier = Modifier,
    state: ParkListScreenState,
    onIntent: (ParkListUserIntent) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                text = "공원",
                onPop = { onIntent(ParkListUserIntent.OnPop) },
            )
        },
        content = { paddingValues ->
            when (state) {
                is ParkListScreenState.Loading -> {
                    LoadingScreen(modifier = modifier.padding(paddingValues))
                }

                is ParkListScreenState.Loaded -> {
                    ParkListScreenLoaded(
                        modifier = modifier.padding(paddingValues),
                        state = state,
                        onIntent = onIntent,
                    )
                }

                is ParkListScreenState.Error -> {
                    ErrorScreen(modifier = modifier.padding(paddingValues), state.message)
                }
            }
        },
    )
}

@Composable
fun ParkListScreenLoaded(
    modifier: Modifier,
    state: ParkListScreenState.Loaded,
    onIntent: (ParkListUserIntent) -> Unit = {},
) {
    val listState = rememberLazyListState()

    Column(
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        PaginatedLazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = listState,
            loadMoreItems = { onIntent(ParkListUserIntent.OnLoadPage) },
        ) {
            items(state.list) { item ->
                ImageCardWithValue(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    icon = Icons.Default.LocationOn,
                    onClick = { onIntent(ParkListUserIntent.OnItemSelect(item.id)) },
                    state = item,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeDetailScreenPreview() {
    ParkListScreenContent(
        state =
            ParkListScreenState.Loaded(
                location = LatLng(128.0, 34.0),
                page = 1,
                list =
                    List(5) {
                        ImageCardWithValueState(
                            id = 1,
                            title = "동락공원",
                            value = "99m",
                            imageUrl = null,
                        )
                    },
            ),
    )
}
