package com.seongmin.pokedex.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface BaseEvent

interface BaseState

interface BaseSideEffect

abstract class BaseViewModel<S : BaseState, E : BaseEvent, SE : BaseSideEffect> : ViewModel() {
    abstract fun initState(): S
    abstract fun handleEvents(event: E)

    private val initialState: S by lazy {
        initState()
    }

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state

    private val _event: MutableSharedFlow<E> = MutableSharedFlow()

    private val _sideEffect: Channel<SE> = Channel()
    val sideEffect: Flow<SE> = _sideEffect.receiveAsFlow()

    init {
        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            _event.collect { event ->
                handleEvents(event = event)
            }
        }
    }

    fun setEvent(event: E) {
        viewModelScope.launch {
            _event.emit(value = event)
        }
    }

    protected fun setState(reducer: S.() -> S) {
        _state.update {
            state.value.reducer()
        }
    }

    protected fun setSideEffect(builder: () -> SE) {
        val effectValue = builder()
        viewModelScope.launch {
            _sideEffect.send(effectValue)
        }
    }
}