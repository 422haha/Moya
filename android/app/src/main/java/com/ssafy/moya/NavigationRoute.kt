package com.ssafy.moya

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object ParkList

@Serializable
object ExploreList

@Serializable
data class ExploreDetail(val itemId: Long)

@Serializable
data class ParkDetail(val parkId: Long)

@Serializable
data class Encyc(val parkId: Long)

@Serializable
data class EncycDetail(val encycId: Long)

@Serializable
data class ExploreStart(val parkId: Long)

@Serializable
data class ARCamera(val explrationId: Long, val parkId: Long)

//TODO 나중에 dataclass로 수정하고 userId 전송하도록
@Serializable
object UserProfileEdit

@Serializable
object Login
