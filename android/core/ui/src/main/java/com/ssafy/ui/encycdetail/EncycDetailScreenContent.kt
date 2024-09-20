package com.ssafy.ui.encycdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.ui.R
import com.ssafy.ui.component.ErrorScreen
import com.ssafy.ui.component.FindButton
import com.ssafy.ui.component.LoadingScreen
import com.ssafy.ui.component.TopBar
import com.ssafy.ui.theme.LightBackgroundColor
import com.ssafy.ui.theme.PrimaryColor
import com.ssafy.ui.theme.jua

@Immutable
data class EncycDetail(
    val plantName: String,
    val plantImage: String?,
    val description: String
)

@Composable
fun EncycDetailScreenContent(
    modifier: Modifier = Modifier,
    encycDetailState: EncycDetailState,
    onIntent: (EncycDetailUserIntent) -> Unit = {}
) {

    DisposableEffect(Unit) {
        onDispose {
            onIntent(EncycDetailUserIntent.OnTTSShutDown)
        }
    }
    Scaffold(
        topBar = {
            TopBar(text = "능소화", PrimaryColor, onPop = { onIntent(EncycDetailUserIntent.OnPop) })
        },
        floatingActionButton = {
            if (encycDetailState is EncycDetailState.Loaded) {
                TTSButton(encycDetailState.data.description, onTTSClicked = { onIntent(EncycDetailUserIntent.OnTTSClicked) })
            }
        },
        content = { paddingValues ->
            when (encycDetailState) {
                is EncycDetailState.Loading -> {
                    LoadingScreen(modifier = modifier.padding(paddingValues))
                }

                is EncycDetailState.Loaded -> {
                    EncycDetailScreenLoaded(
                        modifier = modifier.padding(paddingValues),
                        state = encycDetailState,
                        onIntent = onIntent
                    )
                }

                is EncycDetailState.Error -> {
                    ErrorScreen(
                        modifier = modifier.padding(paddingValues),
                        encycDetailState.message
                    )
                }
            }
        },
        bottomBar = {
            FindButton("찾으러 가기", {})
        }
    )
}

@Composable
fun EncycDetailScreenLoaded(
    modifier: Modifier,
    state: EncycDetailState.Loaded,
    onIntent: (EncycDetailUserIntent) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box {
            ImageSection()
            ButtonSection(modifier = Modifier.align(Alignment.BottomCenter), onIntent)
        }
        Spacer(modifier = Modifier.height(16.dp))

        TitleAndDividerSection("소개")
        DescriptionSection(state.data.description)
        Spacer(modifier = Modifier.height(16.dp))
    }
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
fun ButtonSection(
    modifier: Modifier = Modifier,
    onIntent: (EncycDetailUserIntent) -> Unit = {}
) {
    Surface(
        modifier = modifier
            .height(60.dp)
            .padding(8.dp)
            .padding(bottom = 16.dp)
            .clickable { onIntent(EncycDetailUserIntent.OnImageButtonClicked) },
        shape = RoundedCornerShape(50),
        color = PrimaryColor,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "도감 사진 보기",
                tint = LightBackgroundColor,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "도감 사진 보기",
                color = LightBackgroundColor
            )
        }
    }
}

@Composable
fun TitleAndDividerSection(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = title,
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
fun DescriptionSection(fullText: String) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        if (expanded) {
            Text(
                text = fullText,
                color = Color.Gray,
                modifier = Modifier.align(alignment = Alignment.Start)
            )
        } else {
            Text(
                text = fullText,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray,
                modifier = Modifier.align(alignment = Alignment.Start)
            )
        }

        Text(
            text = if (expanded) "접기" else "더보기",
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 4.dp)
                .clickable { expanded = !expanded },
            style = TextStyle(color = PrimaryColor, fontFamily = jua),
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun TTSButton(textToRead: String, onTTSClicked: (String) -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        IconButton(
            onClick = { onTTSClicked(textToRead) },
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
    EncycDetailScreenContent(
        encycDetailState = EncycDetailState.Loaded(
            EncycDetail(
                plantName = "능소화",
                plantImage = null,
                description = "\"능소화는 중국이 원산인 덩굴나무로 다른 물체에 붙어 올라가 10m까지도 자란다. 추위에 약하여 우리나라에서는 남부지방에서 주로 심어 기르고 있다. 능소화(凌霄花)는 ‘하늘을 능가하는 꽃’이란 뜻이다. 오래 전에 중국에서 들여온 식물로 우리나라에서는 양반들이 이 나무를 아주 좋아해서 ‘양반꽃’이라고도 했으며, 평민들은 이 나무를 함부로 심지 못하게 했다고 한다. 지금은 남부지방을 중심으로 사찰 담장이나 가정집 정원에서 많이 볼 수 있는 관상수가 되었다.\""
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ImageSectionPreview() {
    ImageSection()
}