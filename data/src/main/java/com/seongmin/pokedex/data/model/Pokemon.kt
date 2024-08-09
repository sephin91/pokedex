package com.seongmin.pokedex.data.model

import com.seongmin.pokedex.data.model.StatType.Companion.getStat
import kotlinx.serialization.Serializable


@Serializable
data class Pokemon(
    val name: String = "",
    val id: Int = 0,
    val height: Int = 0,
    val weight: Int = 0,
    val types: List<Types> = emptyList(),
    val stats: List<Stats> = emptyList(),
    val imageUrl: String = "",
    val dominantColor: Int = 0
) {
    val displayName =
        "${
            name.firstOrNull()
                ?.uppercaseChar() ?: ""
        }${name.drop(n = 1)}"
}

@Serializable
data class Types(val type: Type)

@Serializable
data class Type(val name: String) {
    val color = when (name) {
        "normal" -> "#A8A77A"
        "fire" -> "#EE8130"
        "water" -> "#6390F0"
        "electric" -> "#F7D02C"
        "grass" -> "#7AC74C"
        "ice" -> "#96D9D6"
        "fighting" -> "#C22E28"
        "poison" -> "#A33EA1"
        "ground" -> "#E2BF65"
        "flying" -> "#A98FF3"
        "psychic" -> "#F95587"
        "bug" -> "#A6B91A"
        "rock" -> "#B6A136"
        "ghost" -> "#735797"
        "dragon" -> "#6F35FC"
        "dark" -> "#705746"
        "steel" -> "#B7B7CE"
        "fairy" -> "#D685AD"
        else -> ""
    }
}

@Serializable
data class Stats(
    val base_stat: Int,
    val stat: Stat
) {
    private val name = stat.name

    private val type = name.getStat()

    val maxStat = type.maxStat

    val color = type.colorString

    val ratio = base_stat.toFloat() / maxStat

    val displayName = type.name
}

@Serializable
data class Stat(val name: String)

enum class StatType(
    val statName: String,
    val colorString: String,
    val maxStat: Int
) {
    HP(
        statName = "hp",
        colorString = "#FF0000",
        maxStat = 255
    ),
    ATK(
        statName = "attack",
        colorString = "#F08030",
        maxStat = 230
    ),
    DEF(
        statName = "defense",
        colorString = "#6890F0",
        maxStat = 230
    ),
    SpATK(
        statName = "special-attack",
        colorString = "#F85888",
        maxStat = 230
    ),
    SpDEF(
        statName = "special-defense",
        colorString = "#78C850",
        maxStat = 230
    ),
    SPD(
        statName = "speed",
        colorString = "#F8D030",
        maxStat = 230
    ),
    NONE(
        statName = "",
        colorString = "",
        maxStat = 0
    );

    companion object {
        fun String.getStat(): StatType = entries.find { it.statName == this } ?: NONE
    }
}