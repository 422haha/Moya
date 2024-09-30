package com.ssafy.network.di

import com.ssafy.network.api.EncyclopediaApi
import com.ssafy.network.api.ExplorationApi
import com.ssafy.network.api.ParkApi
import com.ssafy.network.api.SeasonApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideEncyclopediaApi(retrofit: Retrofit): EncyclopediaApi {
        return retrofit.create(EncyclopediaApi::class.java)
    }

    @Provides
    fun provideExplorationApi(retrofit: Retrofit): ExplorationApi {
        return retrofit.create(ExplorationApi::class.java)
    }

    @Provides
    fun provideParkApi(retrofit: Retrofit): ParkApi {
        return retrofit.create(ParkApi::class.java)
    }

    @Provides
    fun provideSeasonApi(retrofit: Retrofit): SeasonApi {
        return retrofit.create(SeasonApi::class.java)
    }
}
