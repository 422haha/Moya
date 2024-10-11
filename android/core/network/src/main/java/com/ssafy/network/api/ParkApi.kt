package com.ssafy.network.api

import com.ssafy.model.Park
import com.ssafy.model.ParkDetail
import com.ssafy.model.ParkList
import com.ssafy.network.ResponseBody
import com.ssafy.network.ResponseListBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ParkApi {
    @GET("/park/parks")
    suspend fun getParkList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): Response<ResponseBody<ParkList>>

    @GET("/park/parks/{parkId}")
    suspend fun getParkDetail(
        @Path("parkId") parkId: Long,
    ): Response<ResponseBody<ParkDetail>>

    @GET("/park/home")
    suspend fun getClosePark(
        @Query("latitude")latitude: Double,
        @Query("longitude")longitude: Double,
    ): Response<ResponseBody<Park>>

    @GET("/park/home/famous")
    suspend fun getFamousParks(
        @Query("latitude")latitude: Double,
        @Query("longitude")longitude: Double,
    ): Response<ResponseListBody<Park>>
}
