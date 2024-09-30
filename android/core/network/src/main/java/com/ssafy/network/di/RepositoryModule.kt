package com.ssafy.network.di

import com.ssafy.network.repository.EncyclopediaRepository
import com.ssafy.network.repository.ExplorationRepository
import com.ssafy.network.repository.ParkRepository
import com.ssafy.network.repository.SeasonRepository
import com.ssafy.network.repositoryImpl.EncyclopediaRepositoryImpl
import com.ssafy.network.repositoryImpl.ExplorationRepositoryImpl
import com.ssafy.network.repositoryImpl.ParkRepositoryImpl
import com.ssafy.network.repositoryImpl.SeasonRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun provideExplorationRepository(explorationRepositoryImpl: ExplorationRepositoryImpl): ExplorationRepository

    @Binds
    @Singleton
    fun provideParkRepository(parkRepositoryImpl: ParkRepositoryImpl): ParkRepository

    @Binds
    @Singleton
    fun provideEncyclopediaRepository(encyclopediaRepositoryImpl: EncyclopediaRepositoryImpl): EncyclopediaRepository

    @Binds
    @Singleton
    fun provideSeasonRepository(seasonRepositoryImpl: SeasonRepositoryImpl): SeasonRepository
}
