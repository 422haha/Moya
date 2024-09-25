package com.ssafy.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ssafy.ui.R
import com.ssafy.ui.component.BoxWithImage
import com.ssafy.ui.component.BoxWithImageState
import com.ssafy.ui.component.ErrorScreen
import com.ssafy.ui.component.LoadingScreen
import com.ssafy.ui.theme.LightBackgroundColor
import com.ssafy.ui.theme.SecondaryColor
import com.ssafy.ui.theme.SecondarySurfaceColor

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
    Column(
        modifier =
            modifier
                .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        HomeTopImage(
            image = "https://cdn.autotribune.co.kr/news/photo/202404/16048_73647_5214.png",
            desctiption = "Moya와 함께 공원을 탐험해보아요",
        )
    }
}

@Composable
fun HomeTopImage(
    modifier: Modifier = Modifier,
    image: String?,
    desctiption: String,
) {
    Box(modifier = modifier.fillMaxWidth().height(200.dp)) {
        AsyncImage(
            model = image,
            contentScale = ContentScale.Crop,
            contentDescription = "홈 상단 이미지",
            modifier = Modifier.fillMaxSize(),
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
        )
        Text(
            text = desctiption,
            fontSize = 24.sp,
            modifier =
                Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp),
        )
    }
}



@Composable
fun UserInfo(
    userName: String,
    userImage: String?,
) {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(8.dp)) {
            Image(
                // TODO : 이미지 로딩 추가
                painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "userImage",
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .size(50.dp),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = userName,
                modifier = Modifier.align(Alignment.CenterVertically),
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun HomeBox(
    text: String,
    outBoxColor: Color,
    inBoxColor: Color,
    onClick: () -> Unit = {},
    icon: Int,
    state: BoxWithImageState,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        color = outBoxColor,
        onClick = onClick,
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = text,
                    modifier =
                        Modifier
                            .padding(vertical = 20.dp)
                            .padding(start = 12.dp)
                            .align(alignment = Alignment.CenterVertically),
                    color = LightBackgroundColor,
                )
                IconButton(
                    onClick = onClick,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Go",
                        tint = LightBackgroundColor,
                        modifier = Modifier.size(32.dp),
                    )
                }
            }

            BoxWithImage(
                color = inBoxColor,
                textColor = outBoxColor,
                onClick = onClick,
                icon = icon,
                state = state,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreenContent(
        homeScreenState =
            HomeScreenState.Loaded(
                userName = "사용자 이름",
                userImage = null,
                parkState =
                    BoxWithImageState(
                        title = "동락공원",
                        info = "2024.09.17",
                        image = null,
                    ),
                exploreState =
                    BoxWithImageState(
                        title = "동락공원",
                        info = "500m",
                        image = null,
                    ),
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

@Preview(showBackground = true)
@Composable
fun HomeBoxPreview() {
    HomeBox(
        text = "헬로",
        SecondaryColor,
        SecondarySurfaceColor,
        icon = R.drawable.baseline_calendar_month_24,
        state =
            BoxWithImageState(
                title = "동락공원",
                info = "2024.09.17",
                image = null,
            ),
    )
}
