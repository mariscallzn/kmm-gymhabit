package com.andymariscal.gymhabit.android.ui.exercise

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andymariscal.gymhabit.app.*

@Composable
fun ExerciseEditor(
    viewModel: ExerciseViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    ExerciseContent(state) {
        viewModel.dispatch(it)
    }
}

@Composable
fun ExerciseContent(
    state: ExerciseState,
    dispatch: (ExerciseAction) -> Unit
) {
    Column {
        CreateExercise(
            state.muscles,
            state.equipments
        ) {
            dispatch.invoke(it)
        }
        ExerciseList(exercise = state.exercises)
    }
}

@Composable
fun CreateExercise(
    muscles: List<UiMuscles>,
    equipments: List<UiEquipment>,
    addExercise: (ExerciseAction.Add) -> Unit
) {
    var exerciseName by remember { mutableStateOf("Exercise name") }
    val selectedMuscles = remember { mutableStateListOf<UiMuscles>() }
    val selectedEquipment = remember { mutableStateListOf<UiEquipment>() }

    Card {
        Column {
            TextField(
                value = exerciseName,
                onValueChange = { exerciseName = it })
            Spacer(modifier = Modifier.padding(4.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(muscles) { item ->
                    Row{
                        var checked by remember { mutableStateOf(false)}
                        Text(text = item.name)
                        Checkbox(checked, {
                            checked = it
                            if(it) selectedMuscles.add(item) else selectedMuscles.remove(item)
                        })
                    }
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(equipments) { item ->
                    Row{
                        var checked by remember { mutableStateOf(false)}
                        Text(text = item.name)
                        Checkbox(checked, {
                            checked = it
                            if(it) selectedEquipment.add(item) else selectedEquipment.remove(item)
                        })
                    }
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Button(onClick = {
                addExercise.invoke(ExerciseAction.Add(
                    UiExercise(
                        name = exerciseName,
                        muscles = selectedMuscles.toList(),
                        equipments = selectedEquipment.toList(),
                        id = null
                    )
                ))
            }) {
                Text(text = "Create")
            }
        }
    }
}

@Composable
fun ExerciseList(exercise: List<UiExercise>) {
    LazyColumn {
        items(exercise) {
            ExerciseItem(it)
        }
    }
}

@Composable
fun ExerciseItem(uiExercise: UiExercise) {
    Column {
        Text(text = uiExercise.name)
        Spacer(modifier = Modifier.padding(4.dp))
        Log.e("Item", "$uiExercise")
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            uiExercise.muscles.forEach { 
                Text(text = it.name)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
           uiExercise.equipments.forEach { 
               Text(text = it.name)
           } 
        }
    }
}

@Preview
@Composable
fun PreviewExerciseItem() {
    ExerciseItem(uiExercise = UiExercise(1,
        "Chest press",
        listOf(UiMuscles(1, "Chest"), UiMuscles(2, "Triceps")),
        listOf(UiEquipment(1, "Barbell"))
    ))
}

@Preview
@Composable
fun PreviewCreateExercise() {
    MaterialTheme {
        CreateExercise(
            PreviewData.muscles,
            PreviewData.equipment
        ){}
    }
}

object PreviewData {
    val muscles = listOf(
        UiMuscles(1, "Chest"),
        UiMuscles(2, "Biceps"),
        UiMuscles(3, "Triceps")
    )

    val equipment = listOf(
        UiEquipment(1, "Dumbbell"),
        UiEquipment(2, "Barbell"),
        UiEquipment(3, "Bench")
    )
}