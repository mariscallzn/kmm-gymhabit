package com.andymariscal.gymhabit.android.ui.routineplan

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andymariscal.gymhabit.app.RoutinePlanAction
import com.andymariscal.gymhabit.app.RoutinePlanState
import com.andymariscal.gymhabit.app.UiExercise
import com.andymariscal.gymhabit.app.UiRoutinePlan

@Composable
fun RoutinePlanEditor(
    viewModel: RoutinePlanViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    RoutinePlanContent(state) {
        viewModel.dispatch(it)
    }
}

@Composable
fun RoutinePlanContent(
    state: RoutinePlanState,
    dispatch: (RoutinePlanAction) -> Unit
) {
    Column {
        CreateRoutinePlan(
            exercises = state.exercises,
            addRoutinePlan = dispatch
        )
        Spacer(modifier = Modifier.padding(4.dp))
        RoutinePlanList(
            routinePlans = state.routines
        )
    }
}

@Composable
fun CreateRoutinePlan(
    exercises: List<UiExercise>,
    addRoutinePlan: (RoutinePlanAction.Add) -> Unit
) {
    var routinePlanName by remember { mutableStateOf("") }
    val selectedExercises = remember { mutableStateListOf<UiExercise>() }

    Card {
        Column {
            TextField(value = routinePlanName, onValueChange = { routinePlanName = it })
            Spacer(modifier = Modifier.padding(4.dp))
            Log.e("RP","Exercises = $exercises")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                items(exercises) { item ->
                    Row {
                        var checked by remember { mutableStateOf(false) }
                        Text(text = item.name)
                        Checkbox(checked, {
                            checked = it
                            if (it) selectedExercises.add(item) else selectedExercises.remove(item)
                        })
                    }
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Button(onClick = {
                addRoutinePlan.invoke(
                    RoutinePlanAction.Add(
                        UiRoutinePlan(
                            id = null,
                            name = routinePlanName,
                            exercises = selectedExercises.toList()
                        )
                    )
                )
            }) {
                Text(text = "Create")
            }
        }
    }
}

@Composable
fun RoutinePlanList(routinePlans: List<UiRoutinePlan>) {
    LazyColumn {
        items(routinePlans) {
            RoutinePlanItem(it)
        }
    }
}

@Composable
fun RoutinePlanItem(uiRoutinePlan: UiRoutinePlan) {
    Column {
        Text(text = uiRoutinePlan.name)
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            uiRoutinePlan.exercises.forEach {
                Text(text = it.name)
            }
        }
        Spacer(modifier = Modifier.padding(4.dp))
    }
}

@Preview
@Composable
fun PreviewRoutinePlanList() {
    RoutinePlanList(PreviewData.routinePlans)
}

@Preview
@Composable
fun PreviewRoutinePlan() {
    CreateRoutinePlan(exercises = PreviewData.exercises) {}
}

@Preview
@Composable
fun PreviewRoutinePlanContent() {
    RoutinePlanContent(state = RoutinePlanState(
        PreviewData.routinePlans,
        PreviewData.exercises
    )){}
}

object PreviewData {
    val exercises = listOf(
        UiExercise(
            1, "Chest Press", com.andymariscal.gymhabit.android.ui.exercise.PreviewData.muscles,
            com.andymariscal.gymhabit.android.ui.exercise.PreviewData.equipment
        ),
        UiExercise(
            2, "Hammer Curls", com.andymariscal.gymhabit.android.ui.exercise.PreviewData.muscles,
            com.andymariscal.gymhabit.android.ui.exercise.PreviewData.equipment
        ),
        UiExercise(
            3, "Lateral", com.andymariscal.gymhabit.android.ui.exercise.PreviewData.muscles,
            com.andymariscal.gymhabit.android.ui.exercise.PreviewData.equipment
        ),
        UiExercise(
            4, "Squads", com.andymariscal.gymhabit.android.ui.exercise.PreviewData.muscles,
            com.andymariscal.gymhabit.android.ui.exercise.PreviewData.equipment
        )
    )

    val routinePlans = listOf(
        UiRoutinePlan(1, "Monday", exercises),
        UiRoutinePlan(2, "Tuesday", exercises),
        UiRoutinePlan(3, "Wednesday", exercises),
    )
}
