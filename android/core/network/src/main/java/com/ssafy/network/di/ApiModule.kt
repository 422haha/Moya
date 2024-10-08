package com.ssafy.network.di

import com.ssafy.network.api.EncyclopediaApi
import com.ssafy.network.api.ExplorationApi
import com.ssafy.network.api.ExploreDiaryApi
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
    fun provideEncyclopediaApi(retrofit: Retrofit): EncyclopediaApi = retrofit.create(EncyclopediaApi::class.java)

    @Provides
    fun provideExplorationApi(retrofit: Retrofit): ExplorationApi = retrofit.create(ExplorationApi::class.java)

    @Provides
    fun provideParkApi(retrofit: Retrofit): ParkApi = retrofit.create(ParkApi::class.java)

    @Provides
    fun provideExploreDiaryApi(retrofit: Retrofit): ExploreDiaryApi = retrofit.create(ExploreDiaryApi::class.java)

    @Provides
    fun provideSeasonApi(retrofit: Retrofit): SeasonApi = retrofit.create(SeasonApi::class.java)
}
