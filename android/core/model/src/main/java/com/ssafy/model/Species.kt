package com.ssafy.model

data class Species(
    val id: Long,
    val name: String,
    val scientificName: String,
    val description: String,
    val imageUrl: String,
    val positions: List<LatLng>,
)

data class SpeciesMinimumInfo(
    val speciesId: Long,
    val speciesName: String,
)
