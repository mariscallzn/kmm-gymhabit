package com.andymariscal.gymhabit.data

import com.andymariscal.gymhabit.inf.now
import db.AppDatabaseQueries

interface WorkoutSetPlanDataSource {
    suspend fun insertWorkoutSetPlan(
        routinePlanExerciseId: Long,
        reps: Int,
        weight: Float,
        weightUnit: String
    ): Long
}

internal class WorkoutSetDataSourceImpl(
    private val dbQuery: AppDatabaseQueries
) : WorkoutSetPlanDataSource {

    override suspend fun insertWorkoutSetPlan(
        routinePlanExerciseId: Long,
        reps: Int,
        weight: Float,
        weightUnit: String
    ): Long = dbQuery.transactionWithResult {
        dbQuery.insertWorkoutSetPlan(
            routinePlanExerciseId,
            reps.toLong(),
            weight,
            weightUnit,
            now()
        )
        dbQuery.lastInsertRowId().executeAsOne()
    }
}