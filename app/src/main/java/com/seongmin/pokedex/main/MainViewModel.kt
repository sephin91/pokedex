package com.seongmin.pokedex.main

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.seongmin.pokedex.base.BaseViewModel
import com.seongmin.pokedex.data.PokeDexRepository
import com.seongmin.pokedex.data.model.PokemonIndex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val pokeDexRepository: PokeDexRepository
) : BaseViewModel<MainContract.State, MainContract.Event, MainContract.SideEffect>() {
    private val _pagingData: MutableStateFlow<PagingData<PokemonIndex>> =
        MutableStateFlow(PagingData.empty())
    val pagingData: StateFlow<PagingData<PokemonIndex>> = _pagingData

    override fun initState(): MainContract.State = MainContract.State()

    override fun handleEvents(event: MainContract.Event) {
        when (event) {
            MainContract.Event.OnCreate -> onCreate()
            is MainContract.Event.OnClickPokemonIndex -> onClickPokemonIndex(
                pokemonIndex = event.pokemonIndex,
                color = event.color
            )
        }
    }

    private fun onCreate() {
        getPokemonIndexList()
    }

    private fun getPokemonIndexList() {
        viewModelScope.launch {
            pokeDexRepository.getPokemonIndexInfo()
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { data ->
                    _pagingData.value = data
                }
        }
    }

    private fun onClickPokemonIndex(
        pokemonIndex: PokemonIndex,
        color: Int
    ) {
        getPokemonDetail(
            pokemonIndex = pokemonIndex,
            color = color
        )
    }

    private fun getPokemonDetail(
        pokemonIndex: PokemonIndex,
        color: Int
    ) {
        viewModelScope.launch {
            val detail = pokeDexRepository.getPokemonDetail(name = pokemonIndex.name)
                .copy(
                    imageUrl = pokemonIndex.imageUrl,
                    dominantColor = color
                )

            setState {
                copy(pokemon = detail)
            }

            setSideEffect {
                MainContract.SideEffect.ShowDetail
            }
        }
    }
}