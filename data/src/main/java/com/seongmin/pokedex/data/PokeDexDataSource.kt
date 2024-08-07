package com.seongmin.pokedex.data

import com.seongmin.pokedex.data.model.PokemonIndexInfo

interface PokeDexDataSource {
    suspend fun getPokemonIndexInfo(key: Int): PokemonIndexInfo
}