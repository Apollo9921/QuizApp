package com.apollo9921.quizrise.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                dispatchersModule,
                localModule,
                networkModule,
                repositoryModule,
                viewModelModule,
                useCaseModule
            )
        }
    }
}