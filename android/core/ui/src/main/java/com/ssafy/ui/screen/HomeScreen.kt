package com.ssafy.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.ui.R
import com.ssafy.ui.component.BoxWithImage
import com.ssafy.ui.theme.LightBackgroundColor
import com.ssafy.ui.theme.PrimaryColor
import com.ssafy.ui.theme.SecondaryColor
import com.ssafy.ui.theme.SecondarySurfaceColor
import com.ssafy.ui.theme.SurfaceColor

@Composable
fun HomeScreen(
    onNavigateToParkList: () -> Unit = {},
    onNavigateToExploreList: () -> Unit = {}
) {
    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                UserInfo()
                HomeBox(
                    text = "나의 모험",
                    SecondaryColor,
                    SecondarySurfaceColor,
                    onNavigateToExploreList,
                    R.drawable.baseline_calendar_month_24
                )
                HomeBox(
                    text = "모험을 떠나요",
                    PrimaryColor,
                    SurfaceColor,
                    onNavigateToParkList,
                    R.drawable.baseline_location_on_24
                )
            }
        }
    )
}


@Composable
fun UserInfo() {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(8.dp)) {
            Image(
                painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "유저 이름",
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .size(50.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "유저이름",
                modifier = Modifier.align(Alignment.CenterVertically),
                fontWeight = FontWeight.Bold
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
    painterResource: Int
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        color = outBoxColor,
        onClick = onClick
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .padding(start = 12.dp)
                        .align(alignment = Alignment.CenterVertically),
                    color = LightBackgroundColor
                )
                IconButton(
                    onClick = onClick,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Go",
                        tint = LightBackgroundColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            BoxWithImage(
                color = inBoxColor,
                textColor = outBoxColor,
                onClick = onClick,
                painterResource = painterResource
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onNavigateToExploreList = {}, onNavigateToParkList = {})
}

@Preview(showBackground = true)
@Composable
fun BoxWithImagePreview() {
    BoxWithImage(
        borderWidth = 4.dp,
        color = SecondarySurfaceColor,
        textColor = SecondaryColor,
        painterResource = R.drawable.baseline_location_on_24
    )
}

@Preview(showBackground = true)
@Composable
fun HomeBoxPreview() {
    HomeBox(
        text = "헬로",
        SecondaryColor,
        SecondarySurfaceColor,
        painterResource = R.drawable.baseline_calendar_month_24
    )
}