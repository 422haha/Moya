package com.ssafy.moya.ai

import android.graphics.RectF

data class Result(
    val classIndex: Int,
    val score: Float,
    val rectF: RectF,
)
