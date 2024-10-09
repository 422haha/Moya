package com.ssafy.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ssafy.ui.R
import com.ssafy.ui.component.EncycCardState
import com.ssafy.ui.component.EncycCircleCard
import com.ssafy.ui.component.ErrorScreen
import com.ssafy.ui.component.GradientBox
import com.ssafy.ui.component.ImageCardWithTitleDescription
import com.ssafy.ui.component.ImageCardWithTitleDescriptionState
import com.ssafy.ui.component.ImageCardWithValue
import com.ssafy.ui.component.ImageCardWithValueState
import com.ssafy.ui.component.LoadingScreen
import com.ssafy.ui.theme.GrayColor
import com.ssafy.ui.theme.LightBackgroundColor
import com.ssafy.ui.theme.SecondaryColor

@Composable
fun HomeScreenContent(
    homeScreenState: HomeScreenState,
    onIntent: (HomeUserIntent) -> Unit = {},
) {
    Scaffold(
        content = { paddingValues ->
            when (homeScreenState) {
                is HomeScreenState.Loading -> {
                    LoadingScreen(modifier = Modifier.padding(paddingValues))
                }

                is HomeScreenState.Loaded -> {
                    HomeScreenLoaded(
                        modifier = Modifier.padding(paddingValues),
                        state = homeScreenState,
                        onIntent = onIntent,
                    )
                }

                is HomeScreenState.Error -> {
                    ErrorScreen(modifier = Modifier.padding(paddingValues), homeScreenState.message)
                }
            }
        },
    )
}

@Composable
fun HomeScreenLoaded(
    modifier: Modifier,
    state: HomeScreenState.Loaded,
    onIntent: (HomeUserIntent) -> Unit = {},
) {
    LazyColumn(
        modifier =
            modifier
                .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        item {
            HomeTopImage(
                image = R.drawable.banner,
                desctiption = "Moya와 함께 공원을 탐험해보아요",
            )
        }
        item {
            HorizontalImageCardLayout(state = state.popularParks, onSelected = { id ->
                onIntent(HomeUserIntent.OnSelectPopularPark(id))
            })
        }
        item { ItemDivider() }
        item {
            VerticalImageCardLayout(state = state.closeParks, onSelected = { id ->
                onIntent(HomeUserIntent.OnSelectClosePark(id))
            }, onMore = {
                onIntent(HomeUserIntent.OnNavigateToParkList)
            })
        }
        item { ItemDivider() }
        item {
            HorizontalCircleCardLayout(
                modifier = Modifier.padding(bottom = 32.dp),
                state = state.plantInSeason,
                onSelected = { id ->
                    onIntent(HomeUserIntent.OnSelectEncyc(id))
                },
            )
        }
    }
}

@Composable
fun ItemDivider() {
    HorizontalDivider(
        thickness = 8.dp,
        color = GrayColor,
    )
}

@Composable
fun HomeTopImage(
    modifier: Modifier = Modifier,
    image: Int?,
    desctiption: String,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(200.dp),
    ) {
        AsyncImage(
            model = image,
            contentScale = ContentScale.Crop,
            contentDescription = "홈 상단 이미지",
            modifier = Modifier.fillMaxSize(),
            placeholder = painterResource(id = R.drawable.park_placeholder),
        )
        GradientBox(
            modifier = Modifier.fillMaxWidth().height(80.dp).align(Alignment.BottomCenter),
            color = LightBackgroundColor,
        )
        Text(
            text = "",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier =
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
        )
    }
}

@Composable
fun HorizontalImageCardLayout(
    modifier: Modifier = Modifier,
    state: List<ImageCardWithTitleDescriptionState> = emptyList(),
    onSelected: (id: Long) -> Unit = {},
) {
    Column(modifier = modifier.padding(vertical = 12.dp)) {
        Text(
            modifier = Modifier.padding(start = 16.dp, bottom = 12.dp),
            text = "인기 공원",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
        )
        LazyRow(
            contentPadding = PaddingValues(start = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(state) { item ->
                ImageCardWithTitleDescription(state = item, onClick = onSelected)
            }
        }
    }
}

@Composable
fun VerticalImageCardLayout(
    modifier: Modifier = Modifier,
    state: List<ImageCardWithValueState> = emptyList(),
    onSelected: (id: Long) -> Unit = {},
    onMore: () -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp),
    ) {
        Row(
            modifier.padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("가까운 공원", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "거리",
                modifier = Modifier.clickable { onMore() },
            )
        }
        for (item in state) {
            ImageCardWithValue(
                modifier = Modifier.padding(bottom = 16.dp),
                state = item,
                icon = Icons.Filled.LocationOn,
                onClick = onSelected,
            )
        }
    }
}

@Composable
fun HorizontalCircleCardLayout(
    modifier: Modifier = Modifier,
    state: List<EncycCardState> = emptyList(),
    onSelected: (id: Long) -> Unit = {},
) {
    Column(modifier = modifier.padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier.padding(start = 16.dp, bottom = 12.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Text("가을", fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = SecondaryColor)
            Text("에는 이걸 찾아보아요", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        }
        LazyRow(
            contentPadding = PaddingValues(start = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(state) { item ->
                EncycCircleCard(state = item, onClick = onSelected)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HorizontalImageCardsPreview() {
    HorizontalImageCardLayout(
        state =
            List(5) {
                (
                    ImageCardWithTitleDescriptionState(
                        id = 1,
                        title = "동락공원",
                        description = "동락공원은 동락동에 위치한 공원입니다.",
                        imageUrl = "",
                    )
                )
            },
    )
}

@Preview(showBackground = true)
@Composable
fun VerticalImageCardsPreview() {
    VerticalImageCardLayout(
        state =
            List(3) {
                (
                    ImageCardWithValueState(
                        id = 1,
                        title = "동락공원",
                        value = "99m",
                        imageUrl = "",
                    )
                )
            },
    )
}

@Preview(showBackground = true)
@Composable
fun HorizontalCircleCardLayoutPreview() {
    HorizontalCircleCardLayout(
        state =
            List(3) {
                (
                    EncycCardState(
                        id = 1,
                        name = "동락공원",
                        imageUrl = "",
                        isDiscovered = true,
                    )
                )
            },
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreenContent(
        homeScreenState =
            HomeScreenState.Loaded(
                popularParks =
                    List(5) { idx ->
                        ImageCardWithTitleDescriptionState(
                            id = idx.toLong(),
                            title = "공원 $idx",
                            description = "동락공원은 동락동에 위치한 공원입니다.",
                        )
                    },
                closeParks =
                    List(3) {
                        ImageCardWithValueState(
                            id = 1,
                            title = "동락공원",
                            value = "99m",
                        )
                    },
                plantInSeason =
                    List(3) {
                        EncycCardState(
                            id = 1,
                            name = "동락공원",
                            imageUrl = "",
                            isDiscovered = true,
                        )
                    },
            ),
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreviewLoading() {
    HomeScreenContent(
        homeScreenState = HomeScreenState.Loading,
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreviewError() {
    HomeScreenContent(
        homeScreenState = HomeScreenState.Error("error message"),
    )
}
