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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
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
import com.ssafy.ui.R
import com.ssafy.ui.component.PlantCard
import com.ssafy.ui.component.TopBar
import com.ssafy.ui.theme.SecondaryColor
import com.ssafy.ui.theme.SecondarySurfaceColor

@Composable
fun ExploreDetailScreen(onItemClicked: (Long) -> Unit = {}, onPop: () -> Unit = {}) {
    Scaffold(
        topBar = { TopBar(text = "동락공원", SecondaryColor, onPop) },
        content = { paddingValue ->
            Column(
                modifier = Modifier
                    .padding(paddingValue)
                    .background(SecondarySurfaceColor)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "지도(내가 걸어온 길이랑 마커 표시)",
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                ExploreInfo()
                HorizontalEncyc(onItemClicked)
            }
        }
    )
}

@Composable
fun ExploreInfo() {
    Box(
        modifier = Modifier
            .background(SecondarySurfaceColor)
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = R.drawable.baseline_calendar_month_24),
                    contentDescription = "캘린더",
                    modifier = Modifier
                        .size(32.dp),
                    tint = SecondaryColor
                )
                Text(
                    text = "2024.09.02", fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = SecondaryColor,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextBox(modifier = Modifier.weight(1f))
                TextBox(modifier = Modifier.weight(1f))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextBox(modifier = Modifier.weight(1f))
                TextBox(modifier = Modifier.weight(1f))
            }

            Text(
                text = "도감 신규 등록",
                modifier = Modifier.padding(horizontal = 8.dp),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TextBox(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .padding(horizontal = 8.dp)
            .padding(vertical = 4.dp)
    ) {
        Text(text = "이동 거리", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = "3km", fontSize = 16.sp, color = Color.Gray)
    }
}

@Composable
fun HorizontalEncyc(onItemClicked: (Long) -> Unit) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(1),
        modifier = Modifier
            .padding(8.dp)
            .background(SecondarySurfaceColor)
    ) {
        items(8) { index ->
            PlantCard(plantName = "능소화", isDiscovered = true, onClick = { onItemClicked(index.toLong()) })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreScreenPreview() {
    ExploreDetailScreen()
}