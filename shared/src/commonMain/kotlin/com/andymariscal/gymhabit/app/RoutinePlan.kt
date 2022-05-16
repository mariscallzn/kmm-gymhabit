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
data class RoutinePlanState(
    val routines: List<UiRoutinePlan>,
    val exercises: List<UiExercise>
) : State

sealed class RoutinePlanAction : Action {
    object InitialLoad : RoutinePlanAction()
    object ShowAll : RoutinePlanAction()
    data class Add(
        val routinePlan: UiRoutinePlan
    ) : RoutinePlanAction()
}

sealed class RoutinePlanEvent : Event
//endregion

//region UI Models
data class UiRoutinePlan(
    val id: Long?,
    val name: String,
    val routinePlanExercise: List<UiRoutinePlanExercise>
)

data class UiRoutinePlanExercise(
    val id: Long?,
    val exercisesSets: Map<UiExercise, List<UiSetPlan>>
)

data class UiSetPlan(
    val reps: String,
    val weight: String,
    val weightUnit: String
)
//endregion

//region Redux Store
class RoutinePlanStore : Store<RoutinePlanState, RoutinePlanAction, RoutinePlanEvent>,
    CoroutineScope by CoroutineScope(Dispatchers.Main) {

    //region Global variables
    private val repository = AppStore.getInstance().provideRepository()
    private val state = MutableStateFlow(RoutinePlanState(emptyList(), emptyList()))
    private val event = MutableSharedFlow<RoutinePlanEvent>()
    //endregion

    //region Store
    override fun observeState(): StateFlow<RoutinePlanState> = state
    override fun observeEvent(): Flow<RoutinePlanEvent> = event

    override fun dispatch(action: RoutinePlanAction) {
        when (action) {
            is RoutinePlanAction.InitialLoad -> {
                launch {
                    val routinePlans = repository.getAllFullRoutinePlans()
                    val exercises = repository.getAllFullExercises()

                    state.value = state.value.copy(
                        routines = routinePlans.map(::convert),
                        exercises = exercises.map(::convert)
                    )
                }
            }
            is RoutinePlanAction.ShowAll -> {
                launch {
                    val routinePlan = repository.getAllFullRoutinePlans()
                    state.value = state.value.copy(
                        routines = routinePlan.map(::convert)
                    )
                }
            }
            is RoutinePlanAction.Add -> {
                launch {
                    val routinePlan = action.routinePlan
                    repository.createRoutinePlan(
                        routinePlan.name,
                        routinePlan.routinePlanExercise.flatMap {
                            it.exercisesSets.map { d ->
                                d.key.id ?: -1
                            }
                        }
                    )
                    dispatch(RoutinePlanAction.ShowAll)
                }
            }
        }
    }
    //endregion

    //region Inner Logic

    //endregion
}

//endregion