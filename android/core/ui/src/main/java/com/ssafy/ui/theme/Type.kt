package com.ssafy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ssafy.ui.R

val jua = FontFamily(Font(R.font.pretendard))

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val customTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = jua,
        fontSize = 20.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = jua,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = jua,
        fontSize = 12.sp
    ),
    titleLarge = TextStyle(
        fontFamily = jua,
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold
    ),
    titleMedium = TextStyle(
        fontFamily = jua,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold
    ),
    titleSmall = TextStyle(
        fontFamily = jua,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    ),
    displayLarge = TextStyle(
        fontFamily = jua,
        fontSize = 28.sp
    ),
    displayMedium = TextStyle(
        fontFamily = jua,
        fontSize = 24.sp
    ),
    displaySmall = TextStyle(
        fontFamily = jua,
        fontSize = 20.sp
    )
)