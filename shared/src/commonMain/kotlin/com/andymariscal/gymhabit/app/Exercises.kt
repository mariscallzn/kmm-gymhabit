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
/**
 * State is the representation of the UI, it's immutable and it needs reducers to represent the new
 * value
 */
data class ExerciseState(
    val exercises: List<UiExercise>,
    val muscles: List<UiMuscles>,
    val equipments: List<UiEquipment>
) : State

/**
 * Actions is any request from the user executed by any action done (Click buttons, refresh, navigate)
 */
sealed class ExerciseAction : Action {
    object InitialLoad : ExerciseAction()
    object ShowAllExercises : ExerciseAction()
    data class Add(
        val exercise: UiExercise
    ) : ExerciseAction()
}

/**
 * Events are disposable events that only happen once and the state is not expected to be changed
 * Navigation for example.
 */
sealed class ExerciseEvent : Event
//endregion

// region UI Models
data class UiExercise(
    val id: Long?,
    val name: String,
    val muscles: List<UiMuscles>,
    val equipments: List<UiEquipment>
)

data class UiMuscles(
    val id: Long,
    val name: String
)

data class UiEquipment(
    val id: Long,
    val name: String
)
//endregion

//region Store
class ExerciseStore : Store<ExerciseState, ExerciseAction, ExerciseEvent>,
    CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val repository = AppStore.getInstance().provideRepository()

    private val state = MutableStateFlow(ExerciseState(emptyList(), emptyList(), emptyList()))
    private val event = MutableSharedFlow<ExerciseEvent>()

    override fun observeState(): StateFlow<ExerciseState> = state
    override fun observeEvent(): Flow<ExerciseEvent> = event

    override fun dispatch(action: ExerciseAction) {
        when (action) {
            is ExerciseAction.ShowAllExercises -> {
                launch {
                    state.value = state.value.copy(
                        exercises = repository.getAllFullExercises().map {
                            UiExercise(
                                it.exercise.id,
                                it.exercise.name,
                                it.muscles.map { uiM -> UiMuscles(uiM.id, uiM.name) },
                                it.equipments.map { uiEq -> UiEquipment(uiEq.id, uiEq.name) })
                        })
                }
            }
            is ExerciseAction.InitialLoad -> {
                launch {
                    val equipments = repository.getAllEquipments()
                    val muscles = repository.getAllMuscles()
                    val exercise = repository.getAllFullExercises()
                    state.value = state.value.copy(
                        exercises = exercise.map {
                            UiExercise(
                                it.exercise.id,
                                it.exercise.name,
                                it.muscles.map { uiM -> UiMuscles(uiM.id, uiM.name) },
                                it.equipments.map { uiEq -> UiEquipment(uiEq.id, uiEq.name) })
                        },
                        muscles = muscles.map {
                            UiMuscles(it.id, it.name)
                        },
                        equipments = equipments.map {
                            UiEquipment(it.id, it.name)
                        })
                }
            }
            is ExerciseAction.Add -> {
                launch {
                    val exercise = action.exercise
                    repository.createExercise(exercise.name,
                        exercise.muscles.map { it.id },
                        exercise.equipments.map { it.id })
                    dispatch(ExerciseAction.ShowAllExercises)
                }
            }
        }
    }
}
//endregion