package com.ssafy.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Species(
    val id: Long,
    val name: String,
    val scientificName: String,
    val description: String,
    val imageUrl: String,
    val positions: List<LatLng>,
)

@Serializable
data class SpeciesMinimumInfo(
    val speciesId: Long,
    val speciesName: String,
)

@Serializable
data class SpeciesWithCollectionInfo(
    val speciesId: Long,
    @SerialName("name")
    val speciesName: String,
    val imageUrl: String,
    val discovered: Boolean,
)

@Serializable
data class SpeciesInSeason(
    @SerialName("speciesId")
    val speciesId: Long,
    @SerialName("name")
    val name: String,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("score")
    val score: Float,
)
