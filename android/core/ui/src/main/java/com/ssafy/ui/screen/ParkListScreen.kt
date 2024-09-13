package com.ssafy.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.ui.R
import com.ssafy.ui.component.BoxWithImage
import com.ssafy.ui.component.DateOrLocation
import com.ssafy.ui.component.TopBar
import com.ssafy.ui.theme.PrimaryColor
import com.ssafy.ui.theme.SurfaceColor

@Composable
fun ParkListScreen(
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
                TopBar(text = "공원", PrimaryColor, onPop)
                IconButton(
                    onClick = { /* TODO 검색기능 */ },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
            ) {
                DateOrLocation(SurfaceColor, PrimaryColor, R.drawable.baseline_my_location_24)
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(4) { index ->
                        Spacer(modifier = Modifier.height(12.dp))
                        BoxWithImage(
                            borderWidth = 4.dp,
                            color = SurfaceColor,
                            textColor = PrimaryColor,
                            painterResource = R.drawable.baseline_location_on_24,
                            onClick = { if (clickable) onItemClicked(index) }
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeDetailScreenPreview() {
    ParkListScreen()
}