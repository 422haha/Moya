package com.example.uiexample.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.uiexample.R
import com.example.uiexample.ui.theme.DarkGrayColor
import com.example.uiexample.ui.theme.GrayColor
import com.example.uiexample.ui.theme.LightBackgroundColor
import com.example.uiexample.ui.theme.PrimaryColor
import com.example.uiexample.ui.theme.StarYellowColor
import com.example.uiexample.ui.theme.SurfaceColor

@Composable
fun EncycScreen() {
    Scaffold(
        topBar = {
            TopBar("도감", PrimaryColor)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                FilterChips()
                EncycGrid(items = List(8) { "능소화" }, modifier = Modifier.weight(1f))  // 그리드
                CollectionProgress(progress = 60.0f)
            }
        }
    )
}

@Composable
fun FilterChipComponent(text: String, selected: Boolean) {
    var isSelected: Boolean by remember {
        mutableStateOf(selected)
    }
    FilterChip(
        selected = isSelected,
        onClick = { isSelected = !isSelected },
        label = {
            Text(
                text = text,
                color = if (isSelected) LightBackgroundColor else PrimaryColor,
                fontWeight = FontWeight.Bold
            )
        },
        enabled = true,
        shape = RoundedCornerShape(16.dp),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = if (isSelected) PrimaryColor else SurfaceColor,
            selectedContainerColor = PrimaryColor,
            labelColor = if (isSelected) LightBackgroundColor else PrimaryColor
        ),
        border = if (!isSelected) {
            BorderStroke(1.dp, PrimaryColor)
        } else {
            null
        }
    )
}

@Composable
fun FilterChips() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
    ) {
        FilterChipComponent(text = "전체", selected = true)
        FilterChipComponent(text = "수집완료", selected = false)
        FilterChipComponent(text = "미발견", selected = false)
    }
}


@Composable
fun EncycGrid(items: List<String>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(4.dp),
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 8.dp)
    ) {
        items(items.size) { index ->
            val item = items[index]
            PlantCard(plantName = item, isDiscovered = item != "미발견 - 능소화")
        }
    }
}

@Composable
fun CollectionProgress(progress: Float) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(PrimaryColor)
    ) {
        Text(
            text = "수집률",
            fontSize = 16.sp,
            color = LightBackgroundColor,
            modifier = Modifier.padding(start = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            LinearProgressIndicator(
                progress = { progress / 100 },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = StarYellowColor,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "$progress%",
                fontWeight = FontWeight.Bold,
                color = LightBackgroundColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EncycScreenPreview() {
    EncycScreen()
}

@Preview(showBackground = true)
@Composable
fun CollectionProgressPreview() {
    CollectionProgress(60.0f)
}