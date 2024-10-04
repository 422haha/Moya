package com.ssafy.moya.ai

import android.graphics.RectF
import android.media.Image

data class Result(
    val classIndex: Int,
    val score: Float,
    val rectF: RectF,
    val image: Image,
)
