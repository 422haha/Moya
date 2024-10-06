package com.ssafy.network.di

import com.ssafy.network.repository.EncyclopediaRepository
import com.ssafy.network.repository.ExplorationRepository
import com.ssafy.network.repository.ExploreDiaryRepository
import com.ssafy.network.repository.ParkRepository
import com.ssafy.network.repository.SeasonRepository
import com.ssafy.network.repository.UploadRepository
import com.ssafy.network.repositoryImpl.EncyclopediaRepositoryImpl
import com.ssafy.network.repositoryImpl.ExplorationRepositoryImpl
import com.ssafy.network.repositoryImpl.ExploreDiaryRepositoryImpl
import com.ssafy.network.repositoryImpl.ParkRepositoryImpl
import com.ssafy.network.repositoryImpl.SeasonRepositoryImpl
import com.ssafy.network.repositoryImpl.UploadRepositoryImpl
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

    @Binds
    @Singleton
    fun provideExploreDiaryRepository(exploreDiaryRepositoryImpl: ExploreDiaryRepositoryImpl): ExploreDiaryRepository

    @Binds
    @Singleton
    fun provideUploadRepository(uploadRepositoryImpl: UploadRepositoryImpl): UploadRepository
}
