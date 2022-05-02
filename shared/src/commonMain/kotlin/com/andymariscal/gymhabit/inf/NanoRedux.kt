package com.andymariscal.gymhabit.inf

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface State
interface Action
interface Event

interface Store<S : State, A : Action, E : Event> {
    fun observeState(): StateFlow<S>
    fun observeEvent(): Flow<E>
    fun dispatch(action: A)
}