package com.andymariscal.gymhabit.android.ui.workoutsetplan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andymariscal.gymhabit.android.ui.routineplan.PreviewData
import com.andymariscal.gymhabit.app.*

@Composable
fun WorkoutSetPlanEditor(
    viewModel: WorkoutSetPlanViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    WorkoutSetPlanContent(state) {
        viewModel.dispatch(it)
    }
}

@Composable
fun WorkoutSetPlanContent(
    state: WorkoutSetPlanState,
    dispatch: (WorkoutSetPlanAction) -> Unit
) {
    RoutinePlan(uiRoutinePlan = state.routinePlan, addSetPlan = dispatch)
}

@Composable
fun RoutinePlan(
    uiRoutinePlan: UiRoutinePlan,
    addSetPlan: (WorkoutSetPlanAction) -> Unit
) {
    Column {
        Text(text = uiRoutinePlan.name)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            items(uiRoutinePlan.routinePlanExercise) {
                it.exercisesSets.forEach { map ->
                    WorkoutSetPlanItem(
                        exercise = map.key,
                        sets = map.value,
                        routinePlanId = uiRoutinePlan.id ?: -1L,
                        routinePlanExerciseId = it.id ?: -1L,
                        addSetPlan = addSetPlan
                    )
                }
            }
        }
        Spacer(modifier = Modifier.padding(4.dp))
    }
}

@Composable
fun SetList(reps: String, weight: String, weightUnit: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = reps)
        Text(text = weight)
        Text(text = weightUnit)
    }
}

@Composable
fun WorkoutSetPlanItem(
    exercise: UiExercise,
    sets: List<UiSetPlan>,
    routinePlanId: Long,
    routinePlanExerciseId: Long,
    addSetPlan: (WorkoutSetPlanAction) -> Unit
) {
    var reps by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var weightUnit by remember { mutableStateOf("") }

    val eName by remember { mutableStateOf(exercise.name)}
    Card {
        Column {
            Text(text = eName)
            OutlinedTextField(
                value = reps,
                onValueChange = { r -> reps = r },
                label = { Text(text = "Reps") })
            OutlinedTextField(
                value = weight,
                onValueChange = { w -> weight = w },
                label = { Text(text = "Weight") })
            OutlinedTextField(
                value = weightUnit,
                onValueChange = { wU -> weightUnit = wU },
                label = { Text(text = "Weight unit") })
            Button(onClick = {
                addSetPlan.invoke(
                    WorkoutSetPlanAction.Add(
                        routinePlanId,
                        routinePlanExerciseId,
                        exercise.id ?: -1L,
                        reps,
                        weight,
                        weightUnit
                    )
                )
            }) {
                Text(text = "Add")
            }
            LazyColumn {
                items(sets) { s ->
                    SetPlanItem(
                        reps = s.reps,
                        weight = s.weight,
                        weightUnit = s.weightUnit
                    )
                }
            }
        }
    }
}

@Composable
fun SetPlanItem(
    reps: String,
    weight: String,
    weightUnit: String
) {
    Text(text = "Reps: $reps, Weight: $weight $weightUnit")
}

@Preview
@Composable
fun RoutinePlanPreview() {
    RoutinePlan(uiRoutinePlan = PreviewData.routinePlans.first()) {}
}