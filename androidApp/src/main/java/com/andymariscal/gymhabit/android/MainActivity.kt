package com.andymariscal.gymhabit.android

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.andymariscal.gymhabit.Greeting
import com.andymariscal.gymhabit.app.ExerciseAction
import com.andymariscal.gymhabit.app.ExerciseStore
import com.andymariscal.gymhabit.database.DatabaseDriverFactory
import com.andymariscal.gymhabit.di.GymHabitCore
import kotlinx.coroutines.*

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv: TextView = findViewById(R.id.text_view)
        tv.text = greet()

        GymHabitCore(DatabaseDriverFactory(this)).startAppFramework()

        GlobalScope.launch {
            val state = ExerciseStore()
            state.dispatch(ExerciseAction.InitialLoad)

            state.observeState().collect {
                Log.e("Andres", "$it")
            }
        }
    }
}
