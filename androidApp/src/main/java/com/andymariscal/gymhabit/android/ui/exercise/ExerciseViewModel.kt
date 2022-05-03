package com.andymariscal.gymhabit.android.ui.exercise

import androidx.lifecycle.ViewModel
import com.andymariscal.gymhabit.app.ExerciseAction
import com.andymariscal.gymhabit.app.ExerciseStore

class ExerciseViewModel: ViewModel() {
    private val store = ExerciseStore()
    val state = store.observeState()

    init {
        dispatch(ExerciseAction.InitialLoad)
    }

    fun dispatch(action: ExerciseAction){
        store.dispatch(action)
    }
}