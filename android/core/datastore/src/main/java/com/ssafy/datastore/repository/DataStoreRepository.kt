package com.ssafy.datastore.repository

interface DataStoreRepository {
    suspend fun saveAccessToken(token: String)

    suspend fun deleteAccessToken()

    suspend fun getAccessToken(): String?
}