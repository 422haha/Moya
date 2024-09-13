package com.ssafy.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.ui.theme.LightBackgroundColor
import com.ssafy.ui.theme.PrimaryColor

@Composable
fun TopBar(text: String, backgroundColor: Color, onPop: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(vertical = 4.dp)
    ) {
        IconButton(
            onClick = onPop,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = LightBackgroundColor
            )
        }

        Text(
            text = text,
            fontSize = 18.sp,
            color = LightBackgroundColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(alignment = Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTopAppBarPreview() {
    TopBar("커스텀 앱바", PrimaryColor, {})
}