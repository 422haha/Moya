package com.ssafy.moya

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object ParkList

@Serializable
object ExploreList

@Serializable
data class ExploreDetail(val itemId: Int)

@Serializable
data class ParkDetail(val itemId: Int)

@Serializable
object Encyc

@Serializable
data class EncycDetail(val itemId: Int)

@Serializable
object ExploreStart
