package com.ssafy.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.ui.component.PlantCard
import com.ssafy.ui.component.TopBar
import com.ssafy.ui.theme.LightBackgroundColor
import com.ssafy.ui.theme.PrimaryColor
import com.ssafy.ui.theme.StarYellowColor
import com.ssafy.ui.theme.SurfaceColor

@Composable
fun EncycScreen(onItemClicked: (Int) -> Unit = {}, onPop: () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopBar("도감", PrimaryColor, onPop)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                FilterChips()
                EncycGrid(
                    items = List(8) { "능소화" },
                    modifier = Modifier.weight(1f),
                    onItemClicked = onItemClicked
                )
                CollectionProgress(progress = 60.0f)
            }
        }
    )
}

@Composable
fun FilterChipComponent(text: String, isSelected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
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
    var selectedChipIndex by remember { mutableIntStateOf(0) }

    val chipLabels = listOf("전체", "수집완료", "미발견")

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
    ) {
        chipLabels.forEachIndexed { index, label ->
            FilterChipComponent(
                text = label,
                isSelected = index == selectedChipIndex,
                onClick = {
                    selectedChipIndex = index
                }
            )
        }
    }
}


@Composable
fun EncycGrid(items: List<String>, modifier: Modifier = Modifier, onItemClicked: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(4.dp),
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 8.dp)
    ) {
        items(items) { item ->
            val index = items.indexOf(item)
            PlantCard(
                plantName = item,
                isDiscovered = item != "미발견 - 능소화",
                onClick = { onItemClicked(index) })
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