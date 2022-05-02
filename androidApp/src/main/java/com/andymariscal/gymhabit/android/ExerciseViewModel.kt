package com.andymariscal.gymhabit.android

import androidx.lifecycle.ViewModel
import com.andymariscal.gymhabit.app.ExerciseAction
import com.andymariscal.gymhabit.app.ExerciseStore

class ExerciseViewModel: ViewModel() {
    private val store = ExerciseStore()
    val state = store.observeState()

    init {
        store.dispatch(ExerciseAction.InitialLoad)
    }
}