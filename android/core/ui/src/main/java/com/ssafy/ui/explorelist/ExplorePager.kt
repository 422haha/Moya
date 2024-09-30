package com.ssafy.ui.explorelist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.ui.exploredetail.ExploreDetail
import com.ssafy.ui.exploredetail.ExploreDetailScreenState
import java.util.Date
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExplorePager(
    exploreDetails: List<ExploreDetail>,
    onIntent: (ExploreListUserIntent) -> Unit = {},
) {
    if (exploreDetails.isNotEmpty()) {
        val pagerState = rememberPagerState(initialPage = 0, pageCount = { exploreDetails.size })

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxHeight(),
        ) { page ->
            val exploreDetail = exploreDetails[page]
            val isSelected = pagerState.currentPage == page

            val filteredOffset =
                if (abs(pagerState.currentPage - page) < 2) {
                    pagerState.currentPageOffsetFraction
                } else {
                    0f
                }

            ExploreDetailItem(
                state = ExploreDetailScreenState.Loaded(exploreDetail),
                isSelected = isSelected,
                offset = filteredOffset,
                onClick = { onIntent(ExploreListUserIntent.OnItemSelect(exploreDetail.id)) },
            )
        }
    } else {
        CircularProgressIndicator(
            modifier = Modifier.padding(24.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExplorePagerPreview() {
    val sampleExploreDetails =
        listOf(
            ExploreDetail(
                distance = 3.0,
                runningTime = 20,
                questCompletedCount = 100,
                registerCount = 8,
                date = Date(),
                id = 1L,
                parkName = "싸피뒷뜰",
                imageUrl = "",
            ),
            ExploreDetail(
                distance = 0.5,
                runningTime = 60,
                questCompletedCount = 100,
                registerCount = 15,
                date = Date(),
                id = 2L,
                parkName = "동락공원",
                imageUrl = "",
            ),
            ExploreDetail(
                distance = 7.8,
                runningTime = 130,
                questCompletedCount = 7000,
                registerCount = 18,
                date = Date(),
                id = 3L,
                parkName = "환경연수원",
                imageUrl = "",
            ),
            ExploreDetail(
                distance = 5.5,
                runningTime = 405,
                questCompletedCount = 3000,
                registerCount = 1200,
                date = Date(),
                id = 4L,
                parkName = "어딘가",
                imageUrl = "",
            ),
        )

    ExplorePager(
        exploreDetails = sampleExploreDetails,
        onIntent = {},
    )
}
