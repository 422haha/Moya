package com.ssafy.network.api

import com.ssafy.model.ParkDetail
import com.ssafy.model.ParkList
import com.ssafy.network.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ParkApi {
    // park/parks?page=1&size=10&latitude=128.0&longitude=32.0
    @GET("/park/parks")
    suspend fun getParkList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): Response<ResponseBody<ParkList>>

    // /park/parks/1
    @GET("/park/parks/{parkId}")
    suspend fun getParkDetail(
        @Path("parkId") parkId: Long,
    ): Response<ResponseBody<ParkDetail>>
}
