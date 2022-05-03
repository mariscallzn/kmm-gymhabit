package com.andymariscal.gymhabit.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import com.andymariscal.gymhabit.database.DatabaseDriverFactory
import com.andymariscal.gymhabit.di.GymHabitCore

//SUPER WIP, Nothing works
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO move this to Application class
        GymHabitCore(DatabaseDriverFactory(this)).startAppFramework()
        setContent {
            MaterialTheme {
                GymHabitApp()
            }
        }
    }
}

