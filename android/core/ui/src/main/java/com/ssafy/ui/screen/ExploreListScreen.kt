package com.ssafy.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.ui.R
import com.ssafy.ui.component.BoxWithImage
import com.ssafy.ui.component.DateOrLocation
import com.ssafy.ui.component.TopBar
import com.ssafy.ui.theme.LightBackgroundColor
import com.ssafy.ui.theme.SecondaryColor
import com.ssafy.ui.theme.SecondarySurfaceColor

@Composable
fun ExploreListScreen(
    onItemClicked: (Int) -> Unit = {},
    onPop: () -> Unit = {}
) {

    var clickable by remember { mutableStateOf(true) }

    BackHandler {
        clickable = false
        onPop()
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                TopBar(text = "나의 모험", SecondaryColor, onPop)
                IconButton(
                    onClick = { /* TODO 검색 기능 */ },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = LightBackgroundColor
                    )
                }
            }
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
            ) {
                items(4) { index ->
                    DateOrLocation(
                        SecondarySurfaceColor,
                        SecondaryColor
                    ) // 날짜에 따라 보여줄 데이터 처리
                    Spacer(modifier = Modifier.height(12.dp))
                    BoxWithImage(
                        borderWidth = 4.dp,
                        color = SecondarySurfaceColor,
                        textColor = SecondaryColor,
                        painterResource = R.drawable.baseline_calendar_month_24,
                        onClick = { if (clickable) onItemClicked(index) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ExploreListScreenPreview() {
    ExploreListScreen(onItemClicked = {})
}