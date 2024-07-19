package com.seongmin.pokedex.main

import androidx.lifecycle.viewModelScope
import com.seongmin.pokedex.base.BaseViewModel
import com.seongmin.pokedex.data.PokeDexRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val pokeDexRepository: PokeDexRepository
) : BaseViewModel<MainContract.State, MainContract.Event, MainContract.SideEffect>() {

    override fun initState(): MainContract.State = MainContract.State()

    override fun handleEvents(event: MainContract.Event) {
        when (event) {
            MainContract.Event.OnCreate -> onCreate()
        }
    }

    private fun onCreate() {
        getPokemonIndexList()
    }

    private fun getPokemonIndexList() {
        viewModelScope.launch {
            val info = pokeDexRepository.getPokemonIndexInfo()

            setState {
                copy(
                    pokemonIndexInfo = info
                )
            }
        }
    }
}