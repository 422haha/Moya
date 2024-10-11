package com.ssafy.ui.exploredetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.ui.R
import com.ssafy.ui.component.ErrorScreen
import com.ssafy.ui.component.LoadingScreen
import com.ssafy.ui.component.TopBar
import com.ssafy.ui.formatDate
import com.ssafy.ui.theme.SecondaryColor
import com.ssafy.ui.theme.SecondarySurfaceColor
import java.util.Date

@Immutable
data class ExploreDetail(
    val id: Long,
    val distance: Double,
    val runningTime: Int,
    val questCompletedCount: Int,
    val registerCount: Int,
    val imageUrl: String,
    val date: Date,
    val parkName: String,
)

@Composable
fun ExploreDetailScreenContent(
    modifier: Modifier = Modifier,
    exploreDetailScreenState: ExploreDetailScreenState,
    onIntent: (ExploreDetailUserIntent) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopBar(
                text = "동락공원",
                onPop = { onIntent(ExploreDetailUserIntent.OnPop) },
            )
        },
        content = { paddingValue ->
            when (exploreDetailScreenState) {
                is ExploreDetailScreenState.Loading -> {
                    LoadingScreen(modifier = modifier.padding(paddingValue))
                }

                is ExploreDetailScreenState.Loaded -> {
                    ExploreDetailScreenLoaded(
                        modifier = modifier.padding(paddingValue),
                        state = exploreDetailScreenState,
                        onIntent = onIntent,
                    )
                }

                is ExploreDetailScreenState.Error -> {
                    ErrorScreen(
                        modifier = modifier.padding(paddingValue),
                        exploreDetailScreenState.message,
                    )
                }
            }
        },
    )
}

@Composable
fun ExploreDetailScreenLoaded(
    modifier: Modifier = Modifier,
    state: ExploreDetailScreenState.Loaded,
    onIntent: (ExploreDetailUserIntent) -> Unit,
) {
    Column(
        modifier =
            modifier
                .background(SecondarySurfaceColor),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "지도(내가 걸어온 길이랑 마커 표시)",
            modifier =
                Modifier
                    .fillMaxWidth(),
            contentScale = ContentScale.Crop,
        )
        ExploreInfo(state)
    }
}

@Composable
fun ExploreInfo(state: ExploreDetailScreenState.Loaded) {
    Box(
        modifier =
            Modifier
                .background(SecondarySurfaceColor)
                .fillMaxWidth(),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painterResource(id = R.drawable.baseline_calendar_month_24),
                    contentDescription = "캘린더",
                    modifier =
                        Modifier
                            .size(32.dp),
                    tint = SecondaryColor,
                )
                Text(
                    text = state.exploreDetail.date.formatDate(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = SecondaryColor,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TextBox(modifier = Modifier.weight(1f), "이동거리", "${state.exploreDetail.distance}km")
                TextBox(
                    modifier = Modifier.weight(1f),
                    "소요시간",
                    "${state.exploreDetail.runningTime}분",
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TextBox(
                    modifier = Modifier.weight(1f),
                    "걸음 수",
                    "${state.exploreDetail.questCompletedCount}개",
                )
                TextBox(
                    modifier = Modifier.weight(1f),
                    "도감 등록 수",
                    "${state.exploreDetail.registerCount}종",
                )
            }

            Text(
                text = "도감 신규 등록",
                modifier = Modifier.padding(horizontal = 8.dp),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun TextBox(
    modifier: Modifier = Modifier,
    titleText: String,
    contentText: String,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier =
            modifier
                .padding(horizontal = 8.dp)
                .padding(vertical = 4.dp),
    ) {
        Text(text = titleText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = contentText, fontSize = 16.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreScreenPreview() {
    ExploreDetailScreenContent(
        exploreDetailScreenState =
            ExploreDetailScreenState.Loaded(
                exploreDetail =
                    ExploreDetail(
                        id = 1,
                        distance = 3.0,
                        runningTime = 20,
                        questCompletedCount = 100,
                        registerCount = 8,
                        date = Date(),
                        parkName = "동락공원",
                        imageUrl = "",
                    ),
            ),
    )
}
