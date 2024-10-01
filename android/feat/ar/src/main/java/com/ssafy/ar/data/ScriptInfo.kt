package com.ssafy.ar.data

data class ScriptInfo(
    val id: Int = 0,
    val description1: String = "",
    val description2: String = "",
    val checkDescription1: String = "",
    val checkDescription2: String = "",
    val completeMessage: String = "",
)

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