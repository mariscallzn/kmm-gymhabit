package com.andymariscal.gymhabit.android.ui.workoutsetplan

import androidx.lifecycle.ViewModel
import com.andymariscal.gymhabit.app.WorkoutSetPlanAction
import com.andymariscal.gymhabit.app.WorkoutSetPlanStore

class WorkoutSetPlanViewModel: ViewModel() {
    private val store = WorkoutSetPlanStore()
    val state = store.observeState()

    init {
        //TODO research how to pass variables across screens
        dispatch(WorkoutSetPlanAction.InitialLoad(1))
    }

    fun dispatch(action: WorkoutSetPlanAction) {
        store.dispatch(action)
    }
}