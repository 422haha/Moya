package com.ssafy.ui.extension

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp

/**
 * 아래에만 그림자를 생성
 */
fun Modifier.bottomShadow(shadow: Dp) =
    this
        .clip(
            GenericShape { size, _ ->
                lineTo(size.width, 0f)
                lineTo(size.width, Float.MAX_VALUE)
                lineTo(0f, Float.MAX_VALUE)
            },
        ).shadow(shadow)
