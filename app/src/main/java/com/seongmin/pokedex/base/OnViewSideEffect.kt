package com.seongmin.pokedex.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> OnViewSideEffectChanged(
    sideEffect: Flow<T>,
    handler: (sideEffect: T) -> Unit
) {
    val eventHandler by rememberUpdatedState(newValue = ViewSideEffectHandler(doHandle = handler))

    LaunchedEffect(key1 = true) {
        sideEffect.collect(collector = eventHandler::onViewSideEffectChanged)
    }
}

class ViewSideEffectHandler<T>(private val doHandle: (T) -> Unit) {
    fun onViewSideEffectChanged(sideEffect: T) {
        doHandle(sideEffect)
    }
}