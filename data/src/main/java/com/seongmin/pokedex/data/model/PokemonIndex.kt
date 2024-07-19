package com.seongmin.pokedex.data.model

import kotlinx.serialization.Serializable
import java.util.Locale

@Serializable
data class PokemonIndexInfo(
    val count: Int = 0,
    val next: String? = "",
    val previous: String? = "",
    val results: List<PokemonIndex> = emptyList()
)

@Serializable
data class PokemonIndex(
    val name: String = "",
    val url: String = ""
) {
    private val index = url.split("/")
        .let { pathList ->
            if (
                pathList.last()
                    .isEmpty()
            ) {
                pathList.dropLast(n = 1)
            } else {
                pathList
            }
        }
        .last()

    val imageUrl =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$index.png"

    val displayName = "${name.firstOrNull()?.toString().orEmpty().uppercase()}${name.drop(n = 1)}"
}
