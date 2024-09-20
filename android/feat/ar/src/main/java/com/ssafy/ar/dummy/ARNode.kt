package com.ssafy.ar.dummy

import com.ssafy.ar.ArData.ARNode
import com.ssafy.ar.ArData.QuestData

val scriptNode: MutableList<QuestData> = mutableListOf(
    QuestData(
        id = "0",
        model = "models/quest.glb",
        title = "",
        prevDescription = "",
        middleDescription = "",
        nextDescription = "",
        prevCheckDescription = "",
        middleCheckDescription = "",
        nextCheckDescription = "",
        completeMessage = "",
    ),
    QuestData(
        id = "1",
        model = "models/wishotter.glb",
        title = "수달의 부탁",
        prevDescription = "집을 지어야 하는데 재료가 없어..\n",
        middleDescription = "솔방울 3개",
        nextDescription = "만 모아줄 수 있어?",
        prevCheckDescription = "집에 쓸 재료인 ",
        middleCheckDescription = "솔방울 3개를 \n",
        nextCheckDescription = "벌써 모아온 거야?",
        completeMessage = "고마워 덕분에\n 예쁜 집을 지었어!",
    ),
    QuestData(
        id = "2",
        model = "models/brownturtle.glb",
        title = "거북이의 부탁",
        prevDescription = "알을 숨겨야 하는데..\n",
        middleDescription = "은행잎 5장",
        nextDescription = "만 모아줄래?",
        prevCheckDescription = "알을 숨길 만한 ",
        middleCheckDescription = "은행잎 5장을\n",
        nextCheckDescription = "벌써 모아온 거야?",
        completeMessage = "고마워 덕분에\n 무사히 알을 숨겼어!",
    ),
    QuestData(
        id = "3",
        model = "models/penguin.glb",
        title = "펭귄의 부탁",
        prevDescription = "심부름 가야 하는데 다리가 아파..\n",
        middleDescription = "나대신 ",
        nextDescription = "다녀와 줄 수 있을까?",
        prevCheckDescription = "심부름을 벌써 ",
        middleCheckDescription = "나대신 \n",
        nextCheckDescription = "다녀온 거야?",
        completeMessage = "고마워 덕분에\n 엄마한테 칭찬 받았어!",
    )
)

val nodes: MutableList<ARNode> = mutableListOf(
    ARNode(
        "0",
        36.10716757434519,
        128.41650682192451,
        73.33939074445516,
        "models/quest.glb",
        false
    ),
    ARNode(
        "1",
        36.10716757434519,
        128.41650682192451,
        73.33939074445516,
        "models/penguin.glb",
        false
    ),
    ARNode(
        "2",
        36.10719340772349,
        128.41647777400757,
        73.54002152141184,
        "models/otter.glb",
        false
    ),
    ARNode(
        "3",
        36.10714848472349,
        128.41645558800757,
        73.54002152141184,
        "models/chick.glb",
        false
    ),
    ARNode(
        "4",
        36.10718419443119,
        128.41647704496236,
        73.54002152141184,
        "models/turtle.glb",
        false
    ),
    ARNode(
        "5",
        36.10718419443119,
        128.41647704496236,
        73.54002152141184,
        "models/unicorn.glb",
        false
    ),
    ARNode(
        "6",
        36.106748456430424,
        128.41639460336677,
        68.46302377991378,
        "models/wishotter.glb",
        false
    ),
    ARNode(
        "7",
        36.10688456844942,
        128.41625326737577,
        68.78246488422155,
        "models/direction.glb",
        false
    ),
    ARNode(
        "8",
        36.10672958995879,
        128.41622445983785,
        67.63452187180519,
        "models/penguin.glb",
        false
    ),
    ARNode(
        "9",
        36.1067327895906,
        128.4162147884974,
        68.18832830246538,
        "models/brownturtle.glb",
        false
    ),
)