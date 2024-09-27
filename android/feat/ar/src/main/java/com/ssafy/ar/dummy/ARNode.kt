package com.ssafy.ar.dummy

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
        middleDescription = "솔방울 1개",
        nextDescription = "만 모아줄 수 있어?",
        prevCheckDescription = "집에 쓸 재료인 ",
        middleCheckDescription = "솔방울 1개",
        nextCheckDescription = "를\n벌써 모아온 거야?",
        completeMessage = "고마워! 덕분에 예쁜 집을 지었어!",
    ),
    QuestData(
        id = "2",
        model = "models/brownturtle.glb",
        title = "거북이의 부탁",
        prevDescription = "알을 숨겨야 하는데..\n",
        middleDescription = "은행잎 1장",
        nextDescription = "만 모아 줄래?",
        prevCheckDescription = "알을 숨길 만한 ",
        middleCheckDescription = "은행잎 1장",
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
        middleDescription = "단풍잎 1장",
        nextDescription = "을 가져와 줄래?",
        prevCheckDescription = "마술쇼 소품으로 사용할\n",
        middleCheckDescription = "단풍잎 1장",
        nextCheckDescription = "을 벌써 모아온 거야?",
        completeMessage = "고마워! 덕분에 무사히 마술 공연을 끝냈어!",
    )
)

val npcs: Map<String, NPCLocation> = mapOf(
    "Location1" to NPCLocation("Location1", 36.1067967, 128.4162641),
    "Location2" to NPCLocation("Location2", 36.1066985, 128.4161918),
    "Location3" to NPCLocation("Location3", 36.1066493, 128.4163712),
    "Location4" to NPCLocation("Location4", 36.1017196, 128.419904),
    "Location5" to NPCLocation("Location5", 36.101726, 128.4199104),
    "Location6" to NPCLocation("Location6", 36.1071543, 128.4165288),
    "Location7" to NPCLocation("Location7", 36.1071598, 128.4165313)
)
