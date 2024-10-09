package com.azzam.protectmyfiles

import android.app.Application
import com.azzam.protectmyfiles.di.repositoryModule
import com.azzam.protectmyfiles.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@App)
            modules(
                repositoryModule,
                viewModelModule,
            )
        }
    }
}