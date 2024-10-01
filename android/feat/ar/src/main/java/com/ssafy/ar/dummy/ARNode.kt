package com.ssafy.ar.dummy

import com.ssafy.ar.data.QuestInfo
import com.ssafy.ar.data.QuestState
import com.ssafy.ar.data.ScriptInfo
import kotlin.random.Random

// 행동
val scripts: Map<Int, ScriptInfo> = mapOf(
    // 모아오기
    1 to ScriptInfo(
        id = 1,
        description1 = "학교 준비물이 필요해!\n",
        description2 = " 1개를 모아줄 수 있어?",
        checkDescription1 = "학교에 가져갈 준비물인\n",
        checkDescription2 = " 1개를 벌써 모아온 거야?",
        completeMessage = "고마워! 덕분에 선생님께 칭찬 받았어!",
    ),
    // 다녀오기
    2 to ScriptInfo(
        id = 2,
        description1 = "심부름을 가야 하는데 다리가 아파..\n",
        description2 = "에 대신 다녀와 줄래?",
        checkDescription1 = "다시 왔구나!\n",
        checkDescription2 = "에 벌써 다녀 온 거야?",
        completeMessage = "고마워! 덕분에 엄마한테 칭찬 받았어!",
    ),
    // 모아오기
    3 to ScriptInfo(
        id = 3,
        description1 = "집을 짓는데 재료가 없어..\n",
        description2 = "1개를 모아줄 수 있어?",
        checkDescription1 = "튼튼한 집을 지을 재료인\n",
        checkDescription2 = "1개를 벌써 모아온 거야?",
        completeMessage = "고마워! 덕분에 예쁜 집을 지었어!",
    ),
    // 모아오기
    4 to ScriptInfo(
        id = 4,
        description1 = "심심해! 장난감이 필요해!\n",
        description2 = "1개를 모아줄 수 있어?",
        checkDescription1 = "내가 재밌게 가지고 놀\n",
        checkDescription2 = "1개를 벌써 모아온 거야?",
        completeMessage = "고마워! 덕분에 재밌게 놀았어!",
    ),
)

val quests: Map<Long, QuestInfo> = mapOf(
    1L to QuestInfo(
        id = 1L,
        npcId = Random.nextLong(1, 13),
        npcPosId = 1,
        questType = Random.nextInt(0, 3),
        latitude = 36.1067967,
        longitude = 128.4162641,
        speciesId = 1L,
        speciesName = "",
        isComplete = QuestState.WAIT,
        isPlace = false,
    ),
    2L to QuestInfo(
        id = 2L,
        npcId = Random.nextLong(1, 13),
        npcPosId = 2,
        questType = Random.nextInt(0, 3),
        latitude = 36.1066985,
        longitude = 128.4161918,
        speciesId = 1L,
        speciesName = "",
        isComplete = QuestState.WAIT,
        isPlace = false,
    ),
    3L to QuestInfo(
        id = 3L,
        npcId = Random.nextLong(1, 13),
        npcPosId = 3,
        questType = Random.nextInt(0, 3),
        latitude = 36.1066493,
        longitude = 128.4163712,
        speciesId = 1L,
        speciesName = "",
        isComplete = QuestState.WAIT,
        isPlace = false,
    ),
    4L to QuestInfo(
        id = 4L,
        npcId = Random.nextLong(1, 13),
        npcPosId = 4,
        questType = Random.nextInt(0, 3),
        latitude = 36.1071543,
        longitude = 128.4165288,
        speciesId = 1L,
        speciesName = "",
        isComplete = QuestState.WAIT,
        isPlace = false,
    ),
    5L to QuestInfo(
        id = 5L,
        npcId = Random.nextLong(1, 13),
        npcPosId = 5,
        questType = Random.nextInt(0, 3),
        latitude = 36.101726,
        longitude = 128.4199104,
        speciesId = 1L,
        speciesName = "",
        isComplete = QuestState.WAIT,
        isPlace = false,
    ),
)
