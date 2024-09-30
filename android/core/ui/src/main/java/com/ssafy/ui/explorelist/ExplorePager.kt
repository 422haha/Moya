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
                onClick = { onIntent(ExploreListUserIntent.OnItemSelect(exploreDetail.id)) }
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
        )

    ExplorePager(
        exploreDetails = sampleExploreDetails,
        onIntent = {}
    )
}
