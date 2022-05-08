package com.andymariscal.gymhabit.app

import com.andymariscal.gymhabit.inf.Action
import com.andymariscal.gymhabit.inf.Event
import com.andymariscal.gymhabit.inf.State
import com.andymariscal.gymhabit.inf.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//region Nano Redux Models
data class WorkoutSetPlanState(
    val routinePlan: UiRoutinePlan
) : State

sealed class WorkoutSetPlanAction : Action {
    data class InitialLoad(
        val routinePlanId: Long
    ) : WorkoutSetPlanAction()

    data class Add(
        val exerciseId: Long
    ) : WorkoutSetPlanAction()
}

sealed class WorkoutSetPlanEvent : Event
//endregion

//region UiModels
data class UiWorkoutSetPlan(val tmp: String)
//endregion

//region Redux Store
class WorkoutSetPlanStore : Store<WorkoutSetPlanState, WorkoutSetPlanAction, WorkoutSetPlanEvent>,
    CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val repository = AppStore.getInstance().provideRepository()
    private val state = MutableStateFlow(WorkoutSetPlanState(UiRoutinePlan(0, "", UiRoutinePlanExercise(0, emptyList()))))
    private val event = MutableSharedFlow<WorkoutSetPlanEvent>()

    //region Store
    override fun observeState(): StateFlow<WorkoutSetPlanState> = state

    override fun observeEvent(): Flow<WorkoutSetPlanEvent> = event

    override fun dispatch(action: WorkoutSetPlanAction) {
        when (action) {
            is WorkoutSetPlanAction.InitialLoad -> {
                launch {
                    val routinePlan = repository.getFullRoutinePlanById(action.routinePlanId)
                    state.value = state.value.copy(
                        routinePlan = convert(routinePlan)
                    )
                }
            }
        }
    }
    //endregion
}
//endregion