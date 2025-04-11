package com.eli.examples.dailypulse.utils

expect class Platform {
    val osName: String
    val osVersion: String
    val deviceModel: String
    val density: Int

    fun lofSystemInfo()
}