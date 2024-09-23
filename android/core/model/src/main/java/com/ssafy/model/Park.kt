package com.ssafy.model

/*
    "parkId": 2,
    "parkName": "동락공원",
    "distance": 7342139,
    "imageUrl": "https://example.com/olympic-park.jpg"
 */

data class ParkList(
    val parks: List<Park>
)

data class Park(
    val parkId: Long,
    val parkName: String,
    val distance: Long,
    val imageUrl: String,
)
