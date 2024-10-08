package com.ssafy.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
    @SerialName("profileImageUrl")
    val imageUrl: String,
)
