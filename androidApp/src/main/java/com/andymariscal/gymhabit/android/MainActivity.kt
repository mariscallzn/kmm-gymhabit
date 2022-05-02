package com.andymariscal.gymhabit.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.andymariscal.gymhabit.Greeting
import com.andymariscal.gymhabit.app.ExerciseState
import com.andymariscal.gymhabit.database.DatabaseDriverFactory
import com.andymariscal.gymhabit.di.GymHabitCore

fun greet(): String {
    return Greeting().greeting()
}

//SUPER WIP, Nothing works
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<ExerciseViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GymHabitCore(DatabaseDriverFactory(this)).startAppFramework()
        setContent {
            MaterialTheme {
                ExerciseScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ExerciseScreen(viewModel: ExerciseViewModel) {
    val state by viewModel.state.collectAsState()
    CreateExercise(state)
}

@Composable
fun CreateExercise(state: ExerciseState) {
    var name by remember { mutableStateOf("") }
    Column {
        TextField(value = name, onValueChange = { name = it} )
        Row {
           Text(text = "Muscles")
           DropdownMenu(expanded = false, onDismissRequest = { /*TODO*/ }) {
               state.muscles.forEach {
                   DropdownMenuItem(onClick = { /*TODO*/ }) {
                       Text(it.name)
                   }
               }
           }
        }
    }
}

@Preview
@Composable
fun MainPreview() {
    ExerciseScreen(ExerciseViewModel())
}

