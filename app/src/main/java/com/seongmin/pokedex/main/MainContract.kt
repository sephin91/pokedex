package com.seongmin.pokedex.main

import com.seongmin.pokedex.base.BaseEvent
import com.seongmin.pokedex.base.BaseSideEffect
import com.seongmin.pokedex.base.BaseState
import com.seongmin.pokedex.data.model.Pokemon
import com.seongmin.pokedex.data.model.PokemonIndex
import com.seongmin.pokedex.data.model.PokemonIndexInfo

class MainContract {
    data class State(
        val name: String = "",
        val pokemonIndexInfo: PokemonIndexInfo = PokemonIndexInfo()
    ) : BaseState

    sealed class Event : BaseEvent {
        data object OnCreate : Event()
        data class OnClickPokemonIndex(val pokemonIndex: PokemonIndex) : Event()
    }

    sealed class SideEffect : BaseSideEffect {
        data class ShowDetail(val detail: Pokemon) : SideEffect()
    }
}