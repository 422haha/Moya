package com.example.uiexample.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.uiexample.R
import com.example.uiexample.ui.theme.SecondaryColor
import com.example.uiexample.ui.theme.SecondarySurfaceColor
import com.example.uiexample.ui.theme.SurfaceColor

@Composable
fun ChallengeDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = SecondaryColor
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "도전과제",
                        color = SecondarySurfaceColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    IconButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = SecondarySurfaceColor,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                ChallengeItem(
                    iconRes = R.drawable.ic_launcher_foreground,
                    text = "솔방울 선물하기"
                )
                Spacer(modifier = Modifier.height(12.dp))
                ChallengeItem(
                    iconRes = R.drawable.ic_launcher_foreground,
                    text = "은행잎 선물하기"
                )
                Spacer(modifier = Modifier.height(12.dp))
                ChallengeItem(
                    iconRes = R.drawable.ic_launcher_foreground,
                    text = "단풍잎 선물하기"
                )
            }
        }
    }
}

@Composable
fun ChallengeItem(iconRes: Int, text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SecondarySurfaceColor)
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = "별 모양",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = text, color = SecondaryColor, fontSize = 16.sp)
            }
            IconButton(onClick = { /* TODO*/ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Back",
                    tint = SecondaryColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChallengeDialogPreview() {
    val showDialog = remember { mutableStateOf(true) }
    if (showDialog.value) {
        ChallengeDialog(onDismiss = { showDialog.value = false })
    }
}