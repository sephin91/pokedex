package com.seongmin.pokedex.data

//import com.seongmin.pokedex.data.model.Pokemon
import com.seongmin.pokedex.data.model.PokemonIndexInfo

interface PokeDexRepository {
    suspend fun getPokemonIndexInfo() : PokemonIndexInfo
//    suspend fun getPokemonInfo(): Pokemon
}