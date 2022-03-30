package com.andymariscal.gymhabit.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.andymariscal.gymhabit.Greeting
import com.andymariscal.gymhabit.app.ExerciseStore
import com.andymariscal.gymhabit.database.DatabaseDriverFactory
import com.andymariscal.gymhabit.di.GymHabitCore

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GymHabitCore(DatabaseDriverFactory(this)).startAppFramework()
        setContent {
            MessageCard(name = "")
        }
    }
}

@Composable
fun MessageCard(name: String) {
    val store = ExerciseStore()
    val uiState by store.observeState().collectAsState()
    Column {
        uiState.equipments.forEach {
            Text(text = it.name)
        }
    }
}

@Preview
@Composable
fun MainPreview() {
    MessageCard(name = "Test")
}

