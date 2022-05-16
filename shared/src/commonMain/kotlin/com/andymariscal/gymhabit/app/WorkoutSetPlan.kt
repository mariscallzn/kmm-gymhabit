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
        val routinePlanId: Long,
        val routinePlanExercisesId: Long,
        val exerciseId: Long,
        val reps: String,
        val weight: String,
        val weightUnit: String,
    ) : WorkoutSetPlanAction()

    data class ShowWorkoutSetPlan(
        val routinePlanId: Long
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
    private val state = MutableStateFlow(
        WorkoutSetPlanState(
            UiRoutinePlan(
                0,
                "",
                listOf(UiRoutinePlanExercise(0, emptyMap()))
            )
        )
    )
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

            is WorkoutSetPlanAction.ShowWorkoutSetPlan -> {
                launch {
                    val routinePlan = repository.getFullRoutinePlanById(action.routinePlanId)
                    state.value = state.value.copy(
                        routinePlan = convert(routinePlan)
                    )
                }
            }

            is WorkoutSetPlanAction.Add -> {
                launch {
                    repository.createWorkoutSetPlan(
                        action.routinePlanExercisesId,
                        action.reps.toIntOrNull() ?: -1,
                        action.weight.toFloatOrNull() ?: -1f,
                        action.weightUnit
                    )
                    dispatch(WorkoutSetPlanAction.ShowWorkoutSetPlan(action.routinePlanId))
                }
            }
        }
    }
    //endregion
}
//endregion