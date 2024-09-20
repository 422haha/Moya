package com.ssafy.model

data class ExplorationData(
    val id: Long,
    val myDiscoveredSpecies: List<Species>,
    val species: List<Species>,
    val npcs: List<Npc>,
)
