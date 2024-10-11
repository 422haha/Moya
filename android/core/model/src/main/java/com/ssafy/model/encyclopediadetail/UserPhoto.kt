package com.ssafy.model.encyclopediadetail

import kotlinx.serialization.Serializable

@Serializable
data class UserPhoto(
    val discoveryTime: String,
    val imageUrl: String,
)
