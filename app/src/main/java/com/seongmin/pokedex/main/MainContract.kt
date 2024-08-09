package com.seongmin.pokedex.main

import com.seongmin.pokedex.base.BaseEvent
import com.seongmin.pokedex.base.BaseSideEffect
import com.seongmin.pokedex.base.BaseState
import com.seongmin.pokedex.data.model.Pokemon
import com.seongmin.pokedex.data.model.PokemonIndex

class MainContract {
    data class State(
        val pokemon: Pokemon = Pokemon()
    ) : BaseState

    sealed class Event : BaseEvent {
        data object OnCreate : Event()
        data class OnClickPokemonIndex(
            val pokemonIndex: PokemonIndex,
            val color: Int
        ) : Event()
    }

    sealed class SideEffect : BaseSideEffect {
        data object ShowDetail : SideEffect()
    }
}