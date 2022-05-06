package com.andymariscal.gymhabit.app

import com.andymariscal.gymhabit.data.model.FullExercise
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
    val exercises: List<UiExercise>
)
//endregion

//region Redux Store
class RoutinePlanStore : Store<RoutinePlanState, RoutinePlanAction, RoutinePlanEvent>,
    CoroutineScope by CoroutineScope(Dispatchers.Main) {

    //region Global variables
    private val repository = AppStore().provideRepository()
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
                        routines = routinePlans.map { rp ->
                            UiRoutinePlan(
                                rp.id, rp.name,
                                rp.exercise.map(::convertExercise)
                            )
                        },
                        exercises = exercises.map(::convertExercise)
                    )
                }
            }
            is RoutinePlanAction.ShowAll -> {
                launch {
                    val routinePlan = repository.getAllFullRoutinePlans()
                    state.value = state.value.copy(
                        routines = routinePlan.map { rp ->
                            UiRoutinePlan(
                                rp.id, rp.name, rp.exercise.map(::convertExercise)
                            )
                        }
                    )
                }
            }
            is RoutinePlanAction.Add -> {
                launch {
                    val routinePlan = action.routinePlan
                    repository.createRoutinePlan(
                        routinePlan.name, routinePlan.exercises.map { it.id ?: -1L }
                    )
                    dispatch(RoutinePlanAction.ShowAll)
                }
            }
        }
    }
    //endregion

    //region Inner Logic
    private fun convertExercise(fullExercise: FullExercise): UiExercise =
        UiExercise(
            fullExercise.exercise.id,
            fullExercise.exercise.name,
            fullExercise.muscles.map { uiM -> UiMuscles(uiM.id, uiM.name) },
            fullExercise.equipments.map { uiEq -> UiEquipment(uiEq.id, uiEq.name) })

    //endregion
}
//endregion