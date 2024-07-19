package com.seongmin.pokedex.network.di

import com.seongmin.pokedex.network.NetworkClient
import com.seongmin.pokedex.network.NetworkClientImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal interface NetworkModule {
    @Binds
    @Singleton
    fun bindsNetworkClient(networkClientImpl: NetworkClientImpl): NetworkClient
}