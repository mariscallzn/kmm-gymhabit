package com.andymariscal.gymhabit.data

import db.AppDatabaseQueries

interface WorkoutSetDataSource

internal class WorkoutSetDataSourceImpl(
    private val dbQuery: AppDatabaseQueries
) : WorkoutSetDataSource {

}