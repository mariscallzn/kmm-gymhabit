package com.andymariscal.gymhabit.app

import com.andymariscal.gymhabit.data.Repository
import com.andymariscal.gymhabit.inf.Action
import com.andymariscal.gymhabit.inf.Effect
import com.andymariscal.gymhabit.inf.State
import com.andymariscal.gymhabit.inf.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//WIP TESTS
data class ExerciseState(
    val exercises: List<UiExercise>,
    val muscles: List<UiMuscles>,
    val equipments: List<UiEquipment>
) : State

sealed class ExerciseAction : Action {
    object LoadCatalogs : ExerciseAction()
    data class Add(
        val exercise: UiExercise
    ) : ExerciseAction()
}

sealed class ExerciseEffect : Effect {

}

//UI Models
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

class ExerciseStore : Store<ExerciseState, ExerciseAction, ExerciseEffect>,
    CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val appStore = AppStore()
    private val repository = appStore.provideRepository()

    private val state = MutableStateFlow(ExerciseState(emptyList(), emptyList(), emptyList()))
    private val sideEffect = MutableSharedFlow<ExerciseEffect>()

    override fun observeState(): StateFlow<ExerciseState> = state
    override fun observeSideEffect(): Flow<ExerciseEffect> = sideEffect

    override fun dispatch(action: ExerciseAction) {
        when (action) {
            is ExerciseAction.LoadCatalogs -> {
                launch {
                    val equipments = repository.getAllEquipments()
                    val muscles = repository.getAllMuscles()
                    state.value = state.value.copy(
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

                }
            }
        }
    }
}