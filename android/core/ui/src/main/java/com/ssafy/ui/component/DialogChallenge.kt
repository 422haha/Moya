package com.ssafy.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.ui.R
import com.ssafy.ui.explorestart.Missions
import com.ssafy.ui.theme.SecondaryColor
import com.ssafy.ui.theme.SecondarySurfaceColor
import com.ssafy.ui.theme.StarOutlineColor
import com.ssafy.ui.theme.StarYellowColor
import com.ssafy.ui.theme.customTypography

@Composable
fun ChallengeDialog(
    missions: List<Missions> = listOf(),
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SecondaryColor,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "도전과제",
                    color = SecondarySurfaceColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center),
                    style = customTypography.displayLarge,
                    fontSize = 32.sp,
                )
                IconButton(
                    onClick = { onDismiss() },
                    modifier = Modifier.align(Alignment.TopEnd),
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = SecondarySurfaceColor,
                        modifier = Modifier.size(32.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            missions.forEach { mission ->
                ChallengeItem(
                    text = mission.missionTitle,
                    onConfirm = onConfirm,
                    isSuccess = mission.isSuccess,
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ChallengeItem(
    text: String,
    onConfirm: () -> Unit,
    isSuccess: Boolean,
) {
    Box(
        modifier =
            Modifier
                .clickable { onConfirm() }
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SecondarySurfaceColor)
                .padding(12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    // TODO 이거 성공했으면 star가 보이도록
                    painter =
                        if (isSuccess) {
                            painterResource(id = R.drawable.baseline_star_24)
                        } else {
                            painterResource(id = R.drawable.baseline_star_border_24)
                        },
                    contentDescription = "별 모양",
                    modifier =
                        Modifier
                            .size(24.dp),
                    tint = if (isSuccess) StarYellowColor else StarOutlineColor,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = text, color = SecondaryColor, fontSize = 16.sp)
            }
            IconButton(onClick = onConfirm) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Back",
                    tint = SecondaryColor,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChallengeDialogPreview() {
    val showDialog = remember { mutableStateOf(true) }
    val sampleMissions =
        listOf(
            Missions(1, "솔방울 선물하기", true),
            Missions(2, "은행잎 선물하기", false),
            Missions(3, "단풍잎 5개 모으기", false),
        )

    if (showDialog.value) {
        ChallengeDialog(
            missions = sampleMissions,
            onDismiss = { showDialog.value = false },
        )
    }
}
