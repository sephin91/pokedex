package com.seongmin.pokedex.data

import androidx.paging.PagingData
import com.seongmin.pokedex.data.model.PokemonIndex
import kotlinx.coroutines.flow.Flow

interface PokeDexRepository {
    suspend fun getPokemonIndexInfo() : Flow<PagingData<PokemonIndex>>
}