package com.seongmin.pokedex.data.di

import com.seongmin.pokedex.data.PokeDexDataSource
import com.seongmin.pokedex.data.PokeDexDataSourceImpl
import com.seongmin.pokedex.data.PokeDexRepository
import com.seongmin.pokedex.data.PokeDexRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {
    @Binds
    @Singleton
    fun bindsPokeDexRepository(pokeDexRepositoryImpl: PokeDexRepositoryImpl): PokeDexRepository

    @Binds
    @Singleton
    fun bindsPokeDexDatasource(pokeDexDataSourceImpl: PokeDexDataSourceImpl): PokeDexDataSource
}