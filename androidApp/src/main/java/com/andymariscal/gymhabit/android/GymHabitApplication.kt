package com.andymariscal.gymhabit.android

import android.app.Application
import com.andymariscal.gymhabit.database.DatabaseDriverFactory
import com.andymariscal.gymhabit.di.GymHabitCore

class GymHabitApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        GymHabitCore(DatabaseDriverFactory(this)).startAppFramework()
    }
}