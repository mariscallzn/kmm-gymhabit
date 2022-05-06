package com.andymariscal.gymhabit.android.ui.routineplan

import androidx.lifecycle.ViewModel
import com.andymariscal.gymhabit.app.RoutinePlanAction
import com.andymariscal.gymhabit.app.RoutinePlanStore

class RoutinePlanViewModel: ViewModel() {
    private val store = RoutinePlanStore()
    val state = store.observeState()

    init {
        dispatch(RoutinePlanAction.InitialLoad)
    }

    fun dispatch(action: RoutinePlanAction){
        store.dispatch(action)
    }
}