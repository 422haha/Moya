package com.ssafy.model

data class ExplorationData(
    val id: Long,
    val myDiscoveredSpecies: List<Species>,
    val species: List<Species>,
    val npcs: List<Npc>,
    val completedQuest: Int,
)

data class ExplorationInitialData(
    val id: Long,
    val myDiscoveredSpecies: List<Species>,
    val species: List<Species>,
    val npcs: List<Npc>,
)

data class ExplorationEndData(
    val explorationId: Long,
)
