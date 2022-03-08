package com.andymariscal.gymhabit.data

import db.Equipment
import db.Muscle

interface Repository {

    @Throws(Exception::class)
    suspend fun getAllMuscles(): List<Muscle>

    @Throws(Exception::class)
    suspend fun getAllEquipments(): List<Equipment>

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

    override suspend fun getAllEquipments(): List<Equipment> =
        catalogsDS.selectAllEquipments()

    override suspend fun createExercise(
        name: String,
        muscleIds: List<Long>,
        equipmentIds: List<Long>
    ) = exerciseDS.insertExercise(name, muscleIds, equipmentIds)

}