package com.sadistictech.home.di

import com.sadistictech.home.data.network.HomeApiService
import com.sadistictech.home.data.repository.HomeRepositoryImpl
import com.sadistictech.home.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeNetworkModule {

    @Provides
    @Singleton
    fun provideHomeApiService(retrofit: Retrofit): HomeApiService =
        retrofit.create(HomeApiService::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(implementation: HomeRepositoryImpl): HomeRepository
}
