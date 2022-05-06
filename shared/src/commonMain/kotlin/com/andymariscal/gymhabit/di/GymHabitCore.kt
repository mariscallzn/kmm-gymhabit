package com.andymariscal.gymhabit.di

import com.andymariscal.gymhabit.data.*
import com.andymariscal.gymhabit.data.CatalogsDataSourceImpl
import com.andymariscal.gymhabit.data.ExerciseDataSourceImpl
import com.andymariscal.gymhabit.data.WorkoutSetDataSourceImpl
import com.andymariscal.gymhabit.database.DatabaseDriverFactory
import db.AppDatabase
import db.AppDatabaseQueries
import org.koin.core.context.startKoin
import org.koin.dsl.module

class GymHabitCore(
    databaseDriverFactory: DatabaseDriverFactory
) {
    private val appModule = module {
        single { AppDatabase(databaseDriverFactory.createDriver()).appDatabaseQueries }
    }

    private val dataSources = module {
        single<CatalogsDataSource> { CatalogsDataSourceImpl(get()) }
        single<ExerciseDataSource> { ExerciseDataSourceImpl(get()) }
        single<WorkoutSetDataSource> { WorkoutSetDataSourceImpl(get()) }
        single<RoutinePlanDataSource> { RoutinePlanDataSourceImpl(get()) }
    }

    private val repositoryModule = module {
        single<Repository> { RepositoryImpl(get(), get(), get(), get()) }
    }

    fun startAppFramework() {
        startKoin {
            modules(appModule, dataSources, repositoryModule)
        }
    }
}