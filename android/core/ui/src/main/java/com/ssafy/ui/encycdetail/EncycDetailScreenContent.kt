package com.ssafy.ui.encycdetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ssafy.ui.R
import com.ssafy.ui.component.BackButton
import com.ssafy.ui.component.ErrorScreen
import com.ssafy.ui.component.FindButton
import com.ssafy.ui.component.LoadingScreen
import com.ssafy.ui.theme.PrimaryColor
import com.ssafy.ui.theme.SurfaceColor
import com.ssafy.ui.theme.jua

@Immutable
data class EncycDetail(
    val plantId: Long,
    val plantName: String,
    val plantImage: String?,
    val description: String,
    val userPhoto: String? = null,
)

@Composable
fun EncycDetailScreenContent(
    modifier: Modifier = Modifier,
    encycDetailState: EncycDetailScreenState,
    onIntent: (EncycDetailUserIntent) -> Unit = {},
) {
    DisposableEffect(Unit) {
        onDispose {
            onIntent(EncycDetailUserIntent.OnTTSShutDown)
        }
    }
    Scaffold(
        topBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                encycDetailState.let { state ->
                    if (state is EncycDetailScreenState.Loaded && state.data.plantImage != null) {
                        ImageSection(imageUrl = state.data.plantImage)
                    } else {
                        ImageSection(imageUrl = "")
                    }
                }
                BackButton(
                    modifier =
                        Modifier
                            .padding(16.dp)
                            .size(32.dp),
                    onClick = { onIntent(EncycDetailUserIntent.OnPop)}
                )
                ButtonSection(
                    modifier = Modifier.align(Alignment.BottomEnd).padding(horizontal = 4.dp),
                    onIntent,
                )
            }
        },
        floatingActionButton = {
            if (encycDetailState is EncycDetailScreenState.Loaded) {
                TTSButton(
                    encycDetailState.data.description,
                    onTTSClicked = { onIntent(EncycDetailUserIntent.OnTTSClicked(encycDetailState.data.description)) },
                )
            }
        },
        content = { paddingValues ->
            when (encycDetailState) {
                is EncycDetailScreenState.Loading -> {
                    LoadingScreen(modifier = modifier.padding(paddingValues))
                }

                is EncycDetailScreenState.Loaded -> {
                    EncycDetailScreenLoaded(
                        modifier = modifier.padding(paddingValues),
                        state = encycDetailState,
                        onIntent = onIntent,
                    )
                }

                is EncycDetailScreenState.Error -> {
                    ErrorScreen(
                        modifier = modifier.padding(paddingValues),
                        encycDetailState.message,
                    )
                }
            }
        },
        bottomBar = {
            FindButton(
                "찾으러 가기",
                onClick = { onIntent(EncycDetailUserIntent.OnExploreButtonClicked) },
            )
        },
    )
}

@Composable
fun EncycDetailScreenLoaded(
    modifier: Modifier,
    state: EncycDetailScreenState.Loaded,
    onIntent: (EncycDetailUserIntent) -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        TitleAndDividerSection("소개")
        DescriptionSection(state.data.description)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ImageSection(imageUrl: String) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth(),
        shadowElevation = 4.dp,
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "도감 사진",
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(250.dp),
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
        )
    }
}

@Composable
fun ButtonSection(
    modifier: Modifier = Modifier,
    onIntent: (EncycDetailUserIntent) -> Unit = {},
) {
    Surface(
        modifier =
            modifier
                .height(60.dp)
                .padding(8.dp)
                .padding(bottom = 16.dp)
                .clickable { onIntent(EncycDetailUserIntent.OnImageButtonClicked) },
        border = BorderStroke(1.dp, PrimaryColor),
        shape = RoundedCornerShape(50),
        color = SurfaceColor,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier =
                Modifier
                    .padding(horizontal = 16.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.flip_image),
                contentDescription = "도감 사진 보기",
                tint = PrimaryColor,
                modifier = Modifier.size(20.dp),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "도감 사진 보기",
                color = PrimaryColor,
            )
        }
    }
}

@Composable
fun TitleAndDividerSection(title: String) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
    ) {
        Text(
            text = title,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = PrimaryColor,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Spacer(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(PrimaryColor)
                    .height(2.dp),
        )
    }
}

@Composable
fun DescriptionSection(fullText: String) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
    ) {
        if (expanded) {
            Text(
                text = fullText,
                color = Color.Gray,
                modifier = Modifier.align(alignment = Alignment.Start),
            )
        } else {
            Text(
                text = fullText,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray,
                modifier = Modifier.align(alignment = Alignment.Start),
            )
        }

        Text(
            text = if (expanded) "접기" else "더보기",
            modifier =
                Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp)
                    .clickable { expanded = !expanded },
            style = TextStyle(color = PrimaryColor, fontFamily = jua),
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun TTSButton(
    textToRead: String,
    onTTSClicked: (String) -> Unit,
) {
    Box(
        modifier =
            Modifier
                .padding(horizontal = 16.dp),
    ) {
        IconButton(
            onClick = { onTTSClicked(textToRead) },
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .size(48.dp)
                    .border(BorderStroke(2.dp, PrimaryColor), shape = RoundedCornerShape(16.dp))
                    .background(SurfaceColor),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.speaker),
                contentDescription = "오디오 재생",
                tint = PrimaryColor,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EncycDetailScreenPreview() {
    EncycDetailScreenContent(
        encycDetailState =
            EncycDetailScreenState.Loaded(
                EncycDetail(
                    plantId = 1,
                    plantName = "능소화",
                    plantImage = null,
                    description = "\"능소화는 중국이 원산인 덩굴나무로 다른 물체에 붙어 올라가 10m까지도 자란다. 추위에 약하여 우리나라에서는 남부지방에서 주로 심어 기르고 있다. 능소화(凌霄花)는 ‘하늘을 능가하는 꽃’이란 뜻이다. 오래 전에 중국에서 들여온 식물로 우리나라에서는 양반들이 이 나무를 아주 좋아해서 ‘양반꽃’이라고도 했으며, 평민들은 이 나무를 함부로 심지 못하게 했다고 한다. 지금은 남부지방을 중심으로 사찰 담장이나 가정집 정원에서 많이 볼 수 있는 관상수가 되었다.\"",
                ),
            ),
    )
}

@Preview(showBackground = true)
@Composable
fun ImageSectionPreview() {
    ImageSection("")
}
