package com.andymariscal.gymhabit.data

import com.andymariscal.gymhabit.data.model.FullExercise
import com.andymariscal.gymhabit.data.model.FullRoutinePlan
import com.andymariscal.gymhabit.inf.now
import db.AppDatabaseQueries

interface RoutinePlanDataSource {
    //TODO This has to be a Full object
    suspend fun selectAllRoutinePlans(): List<FullRoutinePlan>

    suspend fun insertRoutinePlan(
        title: String,
        exercises: List<Long>
    ): Long

    suspend fun insertWorkoutSetPlan(
        routinePlanExerciseId: Long,
        reps: Int,
        weight: Float,
        weightUnit: String
    ): Long
}

class RoutinePlanDataSourceImpl(
    private val dbQuery: AppDatabaseQueries
) : RoutinePlanDataSource {

    override suspend fun selectAllRoutinePlans(): List<FullRoutinePlan> =
        dbQuery.transactionWithResult {
            dbQuery.selectAllRoutinePlans().executeAsList().map { rp ->
                FullRoutinePlan(rp.id, rp.title,
                exercise = dbQuery.getAllExercisesByRoutinePlanId(rp.id).executeAsList().map {
                    FullExercise(
                        it,
                        dbQuery.selectMusclesByExerciseId(it.id).executeAsList(),
                        dbQuery.selectEquipmentsByExerciseId(it.id).executeAsList()
                    )
                })
            }
        }

    override suspend fun insertRoutinePlan(
        title: String,
        exercises: List<Long>
    ): Long = dbQuery.transactionWithResult {
        val datetime = now()
        dbQuery.insertRoutinePlan(title, datetime)
        val routinePlanId = dbQuery.lastInsertRowId().executeAsOne()
        exercises.forEach {
            dbQuery.insertRoutinePlanExercises(routinePlanId, it, datetime)
        }
        routinePlanId
    }

    override suspend fun insertWorkoutSetPlan(
        routinePlanExerciseId: Long,
        reps: Int,
        weight: Float,
        weightUnit: String
    ): Long = dbQuery.transactionWithResult {
        val datetime = now()
        dbQuery.insertWorkoutSetPlan(
            routinePlanExerciseId,
            reps.toLong(),
            weight,
            weightUnit,
            datetime
        )
        dbQuery.lastInsertRowId().executeAsOne()
    }
}