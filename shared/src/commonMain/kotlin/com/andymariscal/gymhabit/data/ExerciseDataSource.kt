package com.andymariscal.gymhabit.data

import com.andymariscal.gymhabit.data.model.FullExercise
import db.*

interface ExerciseDataSource {
    suspend fun insertExercise(
        name: String,
        muscleIds: List<Long>,
        equipmentIds: List<Long>
    ): Long

    suspend fun selectAllExercises(): List<Exercise>

    suspend fun selectAllFullExercises(): List<FullExercise>
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
            println("muscle id: $it")
            dbQuery.insertExerciseMuscle(exerciseId, it)
        }
        equipmentIds.forEach {
            println("equipment id: $it")
            dbQuery.insertExerciseEquipment(exerciseId, it)
        }
        exerciseId
    }

    override suspend fun selectAllExercises(): List<Exercise> = dbQuery.transactionWithResult {
        dbQuery.selectAllExercises().executeAsList()
    }

    override suspend fun selectAllFullExercises(): List<FullExercise> =
        dbQuery.transactionWithResult {
            dbQuery.selectAllExercises().executeAsList().map {
                FullExercise(
                    it,
                    dbQuery.selectMusclesByExerciseId(it.id).executeAsList(),
                    dbQuery.selectEquipmentsByExerciseId(it.id).executeAsList()
                )
            }
        }
}