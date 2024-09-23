package com.ssafy.ar.dummy

import com.google.android.gms.maps.model.LatLng
import com.ssafy.ar.data.ARNode
import com.ssafy.ar.data.NPCLocation
import com.ssafy.ar.data.QuestData

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
        middleCheckDescription = "솔방울 3개",
        nextCheckDescription = "를\n벌써 모아온 거야?",
        completeMessage = "고마워! 덕분에 예쁜 집을 지었어!",
    ),
    QuestData(
        id = "2",
        model = "models/brownturtle.glb",
        title = "거북이의 부탁",
        prevDescription = "알을 숨겨야 하는데..\n",
        middleDescription = "은행잎 5장",
        nextDescription = "만 모아 줄래?",
        prevCheckDescription = "알을 숨길 만한 ",
        middleCheckDescription = "은행잎 5장",
        nextCheckDescription = "을\n벌써 모아온 거야?",
        completeMessage = "고마워! 덕분에 무사히 알을 숨겼어!",
    ),
    QuestData(
        id = "3",
        model = "models/penguin.glb",
        title = "펭귄의 부탁",
        prevDescription = "심부름 가야 하는데 다리가 아파..\n",
        middleDescription = "나대신",
        nextDescription = " 다녀와 줄 수 있을까?",
        prevCheckDescription = "심부름을 벌써 ",
        middleCheckDescription = "나대신",
        nextCheckDescription = "\n다녀온 거야?",
        completeMessage = "고마워! 덕분에 엄마한테 칭찬 받았어!",
    ),
    QuestData(
        id = "4",
        model = "models/magicracoon.glb",
        title = "마법사 너구리의 부탁",
        prevDescription = "마술쇼 재료가 필요해!\n",
        middleDescription = "단풍잎 5장",
        nextDescription = "을 가져와 줄래?",
        prevCheckDescription = "마술쇼 소품으로 사용할\n",
        middleCheckDescription = "단풍잎 5장",
        nextCheckDescription = "을 벌써 모아온 거야?",
        completeMessage = "고마워! 덕분에 무사히 마술 공연을 끝냈어!",
    )
    
)

val npcs: Map<String, NPCLocation> = mapOf(
    "Location4" to NPCLocation("Location4", LatLng(36.1017196, 128.419904), isPlace = false),
    "Location5" to NPCLocation("Location5", LatLng(36.101726, 128.4199104), isPlace = false),
    "Location1" to NPCLocation("Location1", LatLng(36.1068254, 128.4162736), isPlace = false),
    "Location2" to NPCLocation("Location2", LatLng(36.1066207, 128.4161508), isPlace = false),
    "Location3" to NPCLocation("Location3", LatLng(36.1066257, 128.4163829), isPlace = false)
)

// 36.1017125
// 128.4199182

val nodes: List<ARNode> = listOf(
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