package com.eli.examples.dailypulse.di

import app.cash.sqldelight.db.SqlDriver
import com.eli.examples.dailypulse.db.DailyPulseDatabase
import com.eli.examples.dailypulse.db.DatabaseDriverFactory
import org.koin.dsl.module

val databaseModule = module {
    single<SqlDriver> { DatabaseDriverFactory().createDriver() }
    single<DailyPulseDatabase> { DailyPulseDatabase(get()) }
}