package com.ssafy.ui.parklist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.ui.R
import com.ssafy.ui.component.BoxWithImage
import com.ssafy.ui.component.BoxWithImageState
import com.ssafy.ui.component.DateOrLocation
import com.ssafy.ui.component.ErrorScreen
import com.ssafy.ui.component.LoadingScreen
import com.ssafy.ui.component.TopBar
import com.ssafy.ui.explorelist.BoxWithImageStateWithData
import com.ssafy.ui.theme.PrimaryColor
import com.ssafy.ui.theme.SurfaceColor

@Composable
fun ParkListScreenContent(
    modifier: Modifier = Modifier,
    state: ParkListScreenState,
    onIntent: (ParkListUserIntent) -> Unit = {},
) {
//    var clickable by remember { mutableStateOf(true) }
//
//    BackHandler {
//        clickable = false
//        onPop()
//    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                TopBar(text = "공원", PrimaryColor, onPop = { onIntent(ParkListUserIntent.OnPop) } )
                IconButton(
                    onClick = { /* TODO 검색기능 */ },
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
            when(state) {
                is ParkListScreenState.Loading -> {
                    LoadingScreen(modifier = modifier.padding(paddingValues))
                }
                is ParkListScreenState.Loaded -> {
                    ParkListScreenLoaded(
                        modifier = modifier.padding(paddingValues),
                        state = state,
                        onIntent = onIntent
                    )
                }
                is ParkListScreenState.Error -> {
                    ErrorScreen(modifier = modifier.padding(paddingValues), state.message)
                }
            }
        }
    )
}

@Composable
fun ParkListScreenLoaded(
    modifier: Modifier,
    state: ParkListScreenState.Loaded,
    onIntent: (ParkListUserIntent) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        DateOrLocation(SurfaceColor, PrimaryColor, R.drawable.baseline_my_location_24)
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(state.list) { item ->
                Spacer(modifier = Modifier.height(12.dp))
                BoxWithImage(
                    borderWidth = 4.dp,
                    color = SurfaceColor,
                    textColor = PrimaryColor,
                    icon = R.drawable.baseline_location_on_24,
                    onClick = { onIntent(ParkListUserIntent.OnItemSelect(item.id)) },
                    state = item.state,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeDetailScreenPreview() {
    ParkListScreenContent(
        state = ParkListScreenState.Loaded(
            list = listOf(
                BoxWithImageStateWithData(
                    id = 1,
                    state = BoxWithImageState(
                        title = "동락공원",
                        info = "2024.09.17",
                        image = null,
                    )
                )
            )
        )
    )
}