package com.andymariscal.gymhabit.data

import db.AppDatabaseQueries

interface ExerciseDataSource {
    suspend fun insertExercise(
        name: String,
        muscleIds: List<Long>,
        equipmentIds: List<Long>
    ): Long
}

internal class ExerciseDataSourceImpl(
    private val dbQuery: AppDatabaseQueries
) : ExerciseDataSource {

    override suspend fun insertExercise(
        name: String,
        muscleIds: List<Long>,
        equipmentIds: List<Long>
    ): Long = dbQuery.transactionWithResult {
        dbQuery.insertExercise(name)
        val exerciseId = dbQuery.lastInsertRowId().executeAsOne()
        muscleIds.forEach {
            dbQuery.insertExerciseMuscle(exerciseId, it)
        }
        equipmentIds.forEach {
            dbQuery.insertExerciseEquipment(exerciseId, it)
        }
        exerciseId
    }
}