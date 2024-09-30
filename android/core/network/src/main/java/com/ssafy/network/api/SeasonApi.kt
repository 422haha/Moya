package com.ssafy.network.api

import com.ssafy.model.SpeciesInSeason
import com.ssafy.network.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface SeasonApi {
    // /season/famous
    @GET("/season/famous")
    suspend fun getFamousSeasons(): Response<ResponseBody<List<SpeciesInSeason>>>
}
