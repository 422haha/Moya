package com.ssafy.ar.dummy

import com.ssafy.ar.data.QuestInfo
import com.ssafy.ar.data.QuestState
import com.ssafy.ar.data.ScriptInfo

val scripts: Map<Int, ScriptInfo> = mapOf(
    0 to ScriptInfo(
        id = 0,
        name = "동물의 부탁",
        questType = 0,
        speciesId = 0,
        speciesName = "",
        prevDescription = "집을 지어야 하는데 재료가 없어..\n",
        middleDescription = "솔방울 1개",
        nextDescription = "만 모아줄 수 있어?",
        prevCheckDescription = "집에 쓸 재료인 ",
        middleCheckDescription = "솔방울 1개",
        nextCheckDescription = "를\n벌써 모아온 거야?",
        completeMessage = "고마워! 덕분에 예쁜 집을 지었어!",
    ),
    1 to ScriptInfo(
        id = 1,
        name = "동물의 부탁",
        questType = 0,
        speciesId = 0,
        speciesName = "",
        prevDescription = "알을 숨겨야 하는데..\n",
        middleDescription = "은행잎 1장",
        nextDescription = "만 모아 줄래?",
        prevCheckDescription = "알을 숨길 만한 ",
        middleCheckDescription = "은행잎 1장",
        nextCheckDescription = "을\n벌써 모아온 거야?",
        completeMessage = "고마워! 덕분에 무사히 알을 숨겼어!",
    ),
    2 to ScriptInfo(
        id = 2,
        name = "동물의 부탁",
        questType = 0,
        speciesId = 0,
        speciesName = "",
        prevDescription = "마술쇼 재료가 필요해!\n",
        middleDescription = "단풍잎 1장",
        nextDescription = "을 가져와 줄래?",
        prevCheckDescription = "마술쇼 소품으로 사용할\n",
        middleCheckDescription = "단풍잎 1장",
        nextCheckDescription = "을 벌써 모아온 거야?",
        completeMessage = "고마워! 덕분에 무사히 마술 공연을 끝냈어!",
    ),
    3 to ScriptInfo(
        id = 3,
        name = "동물의 부탁",
        questType = 0,
        speciesId = 0,
        speciesName = "",
        prevDescription = "심부름 가야 하는데 다리가 아파..\n",
        middleDescription = "나대신",
        nextDescription = " 다녀와 줄 수 있을까?",
        prevCheckDescription = "심부름을 벌써 ",
        middleCheckDescription = "나대신",
        nextCheckDescription = "\n다녀온 거야?",
        completeMessage = "고마워! 덕분에 엄마한테 칭찬 받았어!",
    )
)

val quests: Map<Long, QuestInfo> = mapOf(
    0L to QuestInfo(0L, 1L, 0, longitude = 36.1067967, latitude = 128.4162641, questType = 0, speciesId = ""),
    1L to QuestInfo(1L, 2L, 1, longitude =36.1066985,latitude = 128.4161918, questType = 1, speciesId = ""),
    2L to QuestInfo(2L, 3L, 2, longitude =36.1066493,latitude = 128.4163712, questType = 2, speciesId = ""),
    3L to QuestInfo(3L, 4L, 3, longitude =36.1071543,latitude = 128.4165288, questType = 3, speciesId = ""),
    4L to QuestInfo(4L, 5L, 4,longitude = 36.1017196, latitude =128.419904, questType = 0, speciesId = ""),
    5L to QuestInfo(
        5L,
        6L,
        5,
        longitude = 36.101726,
        latitude = 128.4199104,
        isComplete = QuestState.PROGRESS,
        questType = 2,
        speciesId = ""
    ),
)