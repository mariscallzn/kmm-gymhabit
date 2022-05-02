package com.andymariscal.gymhabit.data

import com.andymariscal.gymhabit.data.model.FullExercise
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
    suspend fun createExercise(
        name: String,
        muscleIds: List<Long>,
        equipmentIds: List<Long>
    ): Long
}

class RepositoryImpl(
    private val catalogsDS: CatalogsDataSource,
    private val exerciseDS: ExerciseDataSource,
    private val workoutSetDS: WorkoutSetDataSource
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

    override suspend fun getAllFullExercises(): List<FullExercise> =
        exerciseDS.selectAllFullExercises()

    @Throws(Exception::class)
    override suspend fun createExercise(
        name: String,
        muscleIds: List<Long>,
        equipmentIds: List<Long>
    ) = exerciseDS.insertExercise(name, muscleIds, equipmentIds)

}