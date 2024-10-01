package com.ssafy.ar.ui

import androidx.compose.runtime.Composable
import com.gowtham.ratingbar.RatingBar

@Composable
fun CustomRatingBar(
    rating: Float
) {
    RatingBar(
        value = rating,
        onValueChange = { },
        onRatingChanged = { },
    )
}