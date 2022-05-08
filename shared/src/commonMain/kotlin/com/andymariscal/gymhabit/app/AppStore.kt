package com.andymariscal.gymhabit.app

import com.andymariscal.gymhabit.data.Repository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class AppStore : KoinComponent {
    private val repository: Repository by inject()
    fun provideRepository(): Repository = repository

    @Suppress("VARIABLE_IN_SINGLETON_WITHOUT_THREAD_LOCAL")
    companion object {
        private var appStore: AppStore? = null
        fun getInstance(): AppStore = appStore?: AppStore().apply {
            appStore = this
        }
    }
}