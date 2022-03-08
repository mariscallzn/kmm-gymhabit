package com.andymariscal.gymhabit.app

import com.andymariscal.gymhabit.data.Repository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class AppStore : KoinComponent {
    private val repository: Repository by inject()
    fun provideRepository(): Repository = repository
}