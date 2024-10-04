package com.ssafy.network.api

import com.ssafy.model.Park
import com.ssafy.model.SpeciesInSeason
import com.ssafy.network.ResponseListBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SeasonApi {
    // /season/famous
    @GET("/season/famous")
    suspend fun getSpeciesInSeason(): Response<ResponseListBody<SpeciesInSeason>>

    // /season/famous/{speciesId}
    @GET("/season/famous/{speciesId}")
    suspend fun getParksBySpeciesId(
        @Path("speciesId") speciesId: Long,
    ): Response<ResponseListBody<Park>>
}
