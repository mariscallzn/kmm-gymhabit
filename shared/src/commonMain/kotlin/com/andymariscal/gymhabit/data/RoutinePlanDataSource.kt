package com.andymariscal.gymhabit.data

import com.andymariscal.gymhabit.data.model.FullExercise
import com.andymariscal.gymhabit.data.model.FullRoutinePlan
import com.andymariscal.gymhabit.data.model.FullRoutinePlanExercises
import com.andymariscal.gymhabit.data.model.FullSetPlan
import com.andymariscal.gymhabit.inf.now
import db.AppDatabaseQueries
import db.RoutinePlan

interface RoutinePlanDataSource {
    suspend fun selectAllFullRoutinePlans(): List<FullRoutinePlan>

    suspend fun selectFullRoutinePlanById(id: Long): FullRoutinePlan

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

    override suspend fun selectAllFullRoutinePlans(): List<FullRoutinePlan> =
        dbQuery.transactionWithResult {
            dbQuery.selectAllRoutinePlans().executeAsList().map(::convert)
        }

    override suspend fun selectFullRoutinePlanById(id: Long): FullRoutinePlan =
        dbQuery.transactionWithResult {
            convert(dbQuery.selectRoutinePlanById(id).executeAsOne())
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

    private fun convert(rp: RoutinePlan): FullRoutinePlan =
        FullRoutinePlan(rp.id, rp.title, dbQuery.selectRoutinePlanExercisesById(rp.id)
            .executeAsList().map { rpe ->
                FullRoutinePlanExercises(rpe.id,
                    dbQuery.selectAllExercisesByRoutinePlanId(rpe.id).executeAsList()
                        .associate {
                            FullExercise(
                                it,
                                dbQuery.selectAllMuscles().executeAsList(),
                                dbQuery.selectAllEquipments().executeAsList()
                            ) to dbQuery.selectWorkoutSetPlanByExerciseId(it.id)
                                .executeAsList().map { wsp ->
                                    FullSetPlan(
                                        wsp.id,
                                        wsp.reps,
                                        wsp.weight,
                                        wsp.weight_unit
                                    )
                                }
                        }
                )
            })
}