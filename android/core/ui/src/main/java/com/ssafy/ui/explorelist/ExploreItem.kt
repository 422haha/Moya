package com.ssafy.ui.explorelist

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.ssafy.ui.exploredetail.ExploreDetail
import com.ssafy.ui.exploredetail.ExploreDetailScreenState
import com.ssafy.ui.exploredetail.TextBox
import com.ssafy.ui.formatDate
import com.ssafy.ui.formatDistance
import com.ssafy.ui.theme.LightBackgroundColor
import com.ssafy.ui.theme.customTypography
import java.util.Date
import kotlin.math.abs
import kotlin.math.min

@Composable
fun ExploreDetailItem(
    state: ExploreDetailScreenState.Loaded,
    isSelected: Boolean,
    offset: Float,
    onClick: (Long) -> Unit = {},
) {
    val animateHeight =
        getOffsetBasedValue(
            selectedValue = 1000,
            nonSelectedValue = 600,
            isSelected = isSelected,
            offset = offset,
        ).dp
    val animateWidth =
        getOffsetBasedValue(
            selectedValue = 640,
            nonSelectedValue = 320,
            isSelected = isSelected,
            offset = offset,
        ).dp
    val animateElevation =
        getOffsetBasedValue(
            selectedValue = 12,
            nonSelectedValue = 2,
            isSelected = isSelected,
            offset = offset,
        ).dp

    Card(
        elevation =
        CardDefaults.elevatedCardElevation(
            animateDpAsState(
                animateElevation,
                label = "",
            ).value,
        ),
        modifier =
        Modifier
            .width(animateWidth)
            .height(animateHeight)
            .padding(24.dp),
        shape = RoundedCornerShape(16.dp),
        colors =
        CardDefaults.cardColors(
            containerColor = LightBackgroundColor,
        ),
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start,
        ) {
            AsyncImage(
                model = state.exploreDetail.imageUrl,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(0.6f),
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
            )
            Column(
                modifier =
                Modifier
                    .padding(8.dp)
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.Top,
            ) {
                Text(
                    text = "동락공원",
                    modifier = Modifier.padding(8.dp),
                    style = customTypography.titleMedium,
                )
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
                        tint = Color.Gray,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = state.exploreDetail.date.formatDate(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    TextBox(
                        modifier = Modifier.weight(1f),
                        "이동거리",
                        state.exploreDetail.distance.toString().formatDistance(),
                    )
                    TextBox(
                        modifier = Modifier.weight(1f),
                        "소요시간",
                        "${state.exploreDetail.runningTime}분",
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    TextBox(
                        modifier = Modifier.weight(1f),
                        "성공한 미션 수",
                        "${state.exploreDetail.questCompletedCount}개",
                    )
                    TextBox(
                        modifier = Modifier.weight(1f),
                        "도감 등록 수",
                        "${state.exploreDetail.registerCount}종",
                    )
                }
            }
        }
    }
}

private fun getOffsetBasedValue(
    selectedValue: Int,
    nonSelectedValue: Int,
    isSelected: Boolean,
    offset: Float,
): Float {
    val actualOffset = if (isSelected) 1 - abs(offset) else abs(offset)
    val delta = abs(selectedValue - nonSelectedValue)
    val offsetBasedDelta = delta * actualOffset

    return min(selectedValue, nonSelectedValue) + offsetBasedDelta
}

@Preview(showBackground = true)
@Composable
fun ExploreDetailItemPreview() {
    ExploreDetailItem(
        state =
        ExploreDetailScreenState.Loaded(
            exploreDetail =
            ExploreDetail(
                id = 0,
                distance = 3000.0,
                runningTime = 20,
                questCompletedCount = 100,
                registerCount = 8,
                date = Date(),
                parkName = "동락공원",
                imageUrl = "",
            ),
        ),
        isSelected = true,
        offset = 0.5f,
    )
}
