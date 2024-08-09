package com.seongmin.pokedex.data.model

import kotlinx.serialization.Serializable


@Serializable
data class Pokemon(
    val name: String = "",
    val id: Int = 0,
    val height: Int = 0,
    val weight: Int = 0,
    val types: List<Types> = emptyList(),
    val stats: List<Stats> = emptyList(),
    val imageUrl: String = ""
)

@Serializable
data class Types(val type: Type)

@Serializable
data class Type(val name: String)

@Serializable
data class Stats(
    val base_stat: Int,
    val stat: Stat
)

@Serializable
data class Stat(val name: String)