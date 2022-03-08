package com.andymariscal.gymhabit.data

import db.AppDatabaseQueries
import db.Equipment
import db.Muscle

interface CatalogsDataSource {
    suspend fun selectAllMuscles(): List<Muscle>
    suspend fun selectAllEquipments(): List<Equipment>
}

internal class CatalogsDataSourceImpl(
    private val dbQuery: AppDatabaseQueries
): CatalogsDataSource {

    override suspend fun selectAllMuscles(): List<Muscle> =
        dbQuery.selectAllMuscles().executeAsList()

    override suspend fun selectAllEquipments(): List<Equipment> =
        dbQuery.selectAllEquipments().executeAsList()
}