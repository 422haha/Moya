package com.example.arcoretest

data class ARNode(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val model: String,
    val isActive: Boolean
)

val nodes: MutableList<ARNode> = mutableListOf(
    ARNode(
        "1",
        36.10716645372349,
        128.41647777400757,
        73.54002152141184,
        "models/otter1.glb",
        true
    ),
    ARNode(
        "2",
        36.10719340772349,
        128.41647777400757,
        73.54002152141184,
        "models/quest.glb",
        true
    ),
    ARNode(
        "3",
        36.10714848472349,
        128.41645558800757,
        73.54002152141184,
        "models/chick.glb",
        true
    ),
    ARNode(
        "4",
        36.10718419443119,
        128.41647704496236,
        73.54002152141184,
        "models/turtle1.glb",
        true
    ),
    ARNode(
        "5",
        36.10718419443119,
        128.41647704496236,
        73.54002152141184,
        "models/turtle1.glb",
        false
    ),
    ARNode(
        "6",
        36.106748456430424,
        128.41639460336677,
        68.46302377991378,
        "models/turtle1.glb",
        true
    ),
    ARNode(
        "7",
        36.10688456844942,
        128.41625326737577,
        68.78246488422155,
        "models/otter1.glb",
        true
    ),
    ARNode(
        "8",
        36.10672958995879,
        128.41622445983785,
        67.63452187180519,
        "models/chick.glb",
        true
    ),
    ARNode(
        "9",
        36.1067327895906,
        128.4162147884974,
        68.18832830246538,
        "models/quest.glb",
        true
    ),
)