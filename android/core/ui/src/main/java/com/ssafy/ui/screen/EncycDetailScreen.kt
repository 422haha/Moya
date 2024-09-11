package com.example.uiexample.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uiexample.R
import com.example.uiexample.ui.theme.LightBackgroundColor
import com.example.uiexample.ui.theme.PrimaryColor

@Composable
fun EncycDetailScreen() {
    Scaffold(
        topBar = {
            TopBar(text = "능소화", PrimaryColor)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Box {
                    ImageSection()
                    ButtonSection(modifier = Modifier.align(Alignment.BottomCenter))
                }
                Spacer(modifier = Modifier.height(16.dp))

                TitleAndDividerSection()
                DescriptionSection()
                Spacer(modifier = Modifier.height(16.dp))

                TTSButton()
            }
        },
        bottomBar = {
            FindButton("찾으러 가기")
        }
    )
}

@Composable
fun ImageSection() {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp),
        shadowElevation = 4.dp,
        color = PrimaryColor
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "도감 사진",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}

@Composable
fun ButtonSection(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .height(60.dp)
            .padding(8.dp)
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(50),
        color = PrimaryColor,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "도감 사진 보기",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "도감 사진 보기",
                color = Color.White
            )
        }
    }
}

@Composable
fun TitleAndDividerSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "소개",
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = PrimaryColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryColor)
                .height(2.dp)
        )
    }
}

@Composable
fun DescriptionSection() {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (expanded) {
                Text(
                    text = "중국 원산의 갈잎 덩굴성 목본식물이다. 단절이름으로... (전체 텍스트가 여기에 들어갑니다)",
                    color = Color.Gray,
                    modifier = Modifier.align(alignment = Alignment.CenterStart)
                )
            } else {
                Text(
                    text = "중국 원산의 갈잎 덩굴성 목본식물이다. 단절이름으로...",
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray,
                    modifier = Modifier.align(alignment = Alignment.CenterStart)
                )
            }

            ClickableText(
                text = AnnotatedString(if (expanded) "접기" else "더보기"),
                onClick = { expanded = !expanded },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(top = 4.dp),
                style = TextStyle(color = PrimaryColor)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun TTSButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        IconButton(
            onClick = { /* TODO */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Gray)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "오디오 재생",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EncycDetailScreenPreview() {
    EncycDetailScreen()
}

@Preview(showBackground = true)
@Composable
fun ImageSectionPreview() {
    ImageSection()
}