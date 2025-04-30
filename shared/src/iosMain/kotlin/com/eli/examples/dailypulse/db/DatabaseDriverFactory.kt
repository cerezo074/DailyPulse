package com.eli.examples.dailypulse.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DatabaseDriverFactory {

    actual fun createDriver(): SqlDriver {
       return NativeSqliteDriver(DailyPulseDatabase.Schema, "dailypulse.db")
    }

}