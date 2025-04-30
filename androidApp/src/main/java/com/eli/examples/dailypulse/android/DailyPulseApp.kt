package com.eli.examples.dailypulse.android

import android.app.Application
import com.eli.examples.dailypulse.android.di.databaseModule
import com.eli.examples.dailypulse.android.di.viewModelsModule
import com.eli.examples.dailypulse.di.sharedKoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DailyPulseApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        val modules = sharedKoinModules + viewModelsModule + databaseModule

        startKoin {
            androidContext(this@DailyPulseApp)
            modules(modules)
        }
    }

}