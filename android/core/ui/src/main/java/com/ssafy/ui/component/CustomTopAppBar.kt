package com.ssafy.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.ui.theme.LightBackgroundColor

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    text: String,
    onPop: () -> Unit,
) {
    Box(
        modifier =
            modifier
                .shadow(4.dp)
                .fillMaxWidth()
                .background(LightBackgroundColor)
                .padding(vertical = 4.dp),
    ) {
        IconButton(
            onClick = onPop,
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Back",
            )
        }

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTopAppBarPreview() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar =
            {
                TopBar(text = "커스텀 앱바", onPop = {})
            },
    ) { paddingValues -> Box(modifier = Modifier.padding(paddingValues)) }
}
