package com.ssafy.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExplorationData(
    @SerialName("explorationId") val id: Long,
    @SerialName("myDiscoveredSpecies") val myDiscoveredSpecies: List<Species>,
    val species: List<Species>,
    val npcs: List<Npc>,
    val completedQuests: Int,
)
@Serializable
data class ExplorationInitialData(
    @SerialName("explorationId")
    val id: Long,
    val myDiscoveredSpecies: List<Species>,
    val species: List<Species>,
    val npcs: List<Npc>,
)
@Serializable
data class ExplorationEndData(
    val explorationId: Long,
)
