package com.ssafy.ui.encyclopedia

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.ui.component.EncycCard
import com.ssafy.ui.component.EncycCardState
import com.ssafy.ui.component.ErrorScreen
import com.ssafy.ui.component.LoadingScreen
import com.ssafy.ui.component.TopBar
import com.ssafy.ui.theme.LightBackgroundColor
import com.ssafy.ui.theme.PrimaryColor
import com.ssafy.ui.theme.StarYellowColor
import com.ssafy.ui.theme.SurfaceColor

val chipLabels = listOf("전체", "수집완료", "미발견")

@Composable
fun EncycScreenContent(
    modifier: Modifier = Modifier,
    encycScreenState: EncycScreenState,
    onIntent: (EncycUserIntent) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopBar("도감", PrimaryColor, onPop = { onIntent(EncycUserIntent.OnPop) })
        },
        content = { paddingValues ->
            when (encycScreenState) {
                is EncycScreenState.Loading -> {
                    LoadingScreen(modifier = modifier.padding(paddingValues))
                }

                is EncycScreenState.Loaded -> {
                    EncycScreenLoaded(
                        modifier = modifier.padding(paddingValues),
                        state = encycScreenState,
                        onIntent = onIntent,
                    )
                }

                is EncycScreenState.Error -> {
                    ErrorScreen(
                        modifier = modifier.padding(paddingValues),
                        encycScreenState.message,
                    )
                }
            }
        },
    )
}

@Composable
fun EncycScreenLoaded(
    modifier: Modifier,
    state: EncycScreenState.Loaded,
    onIntent: (EncycUserIntent) -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        FilterChips(
            selectedChipIndex = state.selectedChipIndex,
            onChipSelected = { index ->
                onIntent(EncycUserIntent.OnChipSelected(index))
            },
        )
        EncycGrid(
            items = state.items,
            modifier = Modifier.weight(1f),
            onItemClicked = { onIntent(EncycUserIntent.OnItemSelect(it)) },
        )
        CollectionProgress(progress = state.progress)
    }
}

@Composable
fun FilterChipComponent(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                color = if (isSelected) LightBackgroundColor else PrimaryColor,
                fontWeight = FontWeight.Bold,
            )
        },
        enabled = true,
        shape = RoundedCornerShape(16.dp),
        colors =
            FilterChipDefaults.filterChipColors(
                containerColor = if (isSelected) PrimaryColor else SurfaceColor,
                selectedContainerColor = PrimaryColor,
                labelColor = if (isSelected) LightBackgroundColor else PrimaryColor,
            ),
        border =
            if (!isSelected) {
                BorderStroke(1.dp, PrimaryColor)
            } else {
                null
            },
    )
}

@Composable
fun FilterChips(
    selectedChipIndex: Int,
    onChipSelected: (Int) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp),
    ) {
        chipLabels.forEachIndexed { index, label ->
            FilterChipComponent(
                text = label,
                isSelected = index == selectedChipIndex,
                onClick = {
                    onChipSelected(index)
                },
            )
        }
    }
}

@Composable
fun EncycGrid(
    items: List<EncycCardState>,
    modifier: Modifier = Modifier,
    onItemClicked: (Long) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(4.dp),
        modifier =
            modifier
                .fillMaxHeight()
                .heightIn(min = 200.dp)
                .padding(horizontal = 8.dp),
    ) {
        itemsIndexed(items) { index, item ->
            EncycCard(
                EncycCardState(
                    id = item.id,
                    name = item.name,
                    imageUrl = item.imageUrl,
                    isDiscovered = item.isDiscovered,
                ),
                onClick = { onItemClicked(index.toLong()) },
            )
        }
    }
}

@Composable
fun CollectionProgress(progress: Float) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(PrimaryColor),
    ) {
        Text(
            text = "수집률",
            fontSize = 16.sp,
            color = LightBackgroundColor,
            modifier = Modifier.padding(start = 8.dp),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
        ) {
            LinearProgressIndicator(
                progress = { progress / 100 },
                modifier =
                    Modifier
                        .fillMaxWidth(0.8f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                color = StarYellowColor,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "$progress%",
                fontWeight = FontWeight.Bold,
                color = LightBackgroundColor,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EncycScreenPreview() {
    EncycScreenContent(
        encycScreenState =
            EncycScreenState.Loaded(
                selectedChipIndex = 0,
                items =
                    List(8) { index ->
                        EncycCardState(
                            id = 1,
                            name = "식물 $index",
                            imageUrl = null,
                            isDiscovered = index % 2 == 0,
                        )
                    },
                progress = 60.0f,
            ),
    )
}

@Preview(showBackground = true)
@Composable
fun CollectionProgressPreview() {
    CollectionProgress(60.0f)
}
