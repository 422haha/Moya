package com.ssafy.network.di

import com.ssafy.network.api.ExplorationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    fun provideExplorationApi(retrofit: Retrofit): ExplorationApi {
        return retrofit.create(ExplorationApi::class.java)
    }
}
