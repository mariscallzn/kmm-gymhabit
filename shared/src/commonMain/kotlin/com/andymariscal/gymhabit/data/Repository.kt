package com.andymariscal.gymhabit.data

import com.andymariscal.gymhabit.data.model.FullExercise
import com.andymariscal.gymhabit.data.model.FullRoutinePlan
import db.Equipment
import db.Exercise
import db.Muscle

interface Repository {

    @Throws(Exception::class)
    suspend fun getAllMuscles(): List<Muscle>

    @Throws(Exception::class)
    suspend fun getAllEquipments(): List<Equipment>

    @Throws(Exception::class)
    suspend fun getAllExercises(): List<Exercise>

    @Throws(Exception::class)
    suspend fun getAllFullExercises(): List<FullExercise>

    @Throws(Exception::class)
    suspend fun getAllFullRoutinePlans(): List<FullRoutinePlan>

    @Throws(Exception::class)
    suspend fun createExercise(
        name: String,
        muscleIds: List<Long>,
        equipmentIds: List<Long>
    ): Long

    @Throws(Exception::class)
    suspend fun createRoutinePlan(
        name: String,
        exercises: List<Long>
    ): Long

    @Throws(Exception::class)
    suspend fun createWorkoutSetPlan(
        routinePlanExerciseId: Long,
        reps: Int,
        weight: Float,
        weightUnit: String
    ): Long
}

class RepositoryImpl(
    private val catalogsDS: CatalogsDataSource,
    private val exerciseDS: ExerciseDataSource,
    private val workoutSetDS: WorkoutSetDataSource,
    private val routinePlanDS: RoutinePlanDataSource
) : Repository {

    @Throws(Exception::class)
    override suspend fun getAllMuscles(): List<Muscle> =
        catalogsDS.selectAllMuscles()

    @Throws(Exception::class)
    override suspend fun getAllEquipments(): List<Equipment> =
        catalogsDS.selectAllEquipments()

    @Throws(Exception::class)
    override suspend fun getAllExercises(): List<Exercise> =
        exerciseDS.selectAllExercises()

    @Throws(Exception::class)
    override suspend fun getAllFullExercises(): List<FullExercise> =
        exerciseDS.selectAllFullExercises()

    @Throws(Exception::class)
    override suspend fun getAllFullRoutinePlans(): List<FullRoutinePlan> =
        routinePlanDS.selectAllRoutinePlans()

    @Throws(Exception::class)
    override suspend fun createExercise(
        name: String,
        muscleIds: List<Long>,
        equipmentIds: List<Long>
    ) = exerciseDS.insertExercise(name, muscleIds, equipmentIds)

    @Throws(Exception::class)
    override suspend fun createRoutinePlan(
        name: String,
        exercises: List<Long>
    ): Long = routinePlanDS.insertRoutinePlan(name, exercises)

    @Throws(Exception::class)
    override suspend fun createWorkoutSetPlan(
        routinePlanExerciseId: Long,
        reps: Int,
        weight: Float,
        weightUnit: String
    ): Long = routinePlanDS.insertWorkoutSetPlan(
        routinePlanExerciseId, reps, weight, weightUnit
    )
}