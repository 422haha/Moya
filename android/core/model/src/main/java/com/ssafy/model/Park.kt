package com.ssafy.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParkList(
    val parks: List<Park>,
)

@Serializable
data class Park(
    val parkId: Long,
    val parkName: String,
    val distance: Long,
    val imageUrl: String,
    val description: String? = null,
)

@Serializable
data class ParkDetail(
    val parkId: Long,
    @SerialName("name")
    val parkName: String,
    val description: String,
    val imageUrl: String,
    val species: List<SpeciesWithCollectionInfo>,
)
