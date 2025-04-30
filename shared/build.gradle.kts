import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:latest_version")
        classpath("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
    }
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("co.touchlab.skie") version ("0.9.0")
    id("com.codingfeline.buildkonfig") version "+"
    kotlin("plugin.serialization") version "2.1.20"
    alias(libs.plugins.sqlDelight)
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.sql.coroutines.extensions)
        }

        androidMain.dependencies {
            implementation(libs.androidx.lifecycle.viewmodel.ktx)
            implementation(libs.ktor.client.android)
            implementation(libs.sql.android.driver)
        }

        iosMain.dependencies  {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sql.native.driver)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

buildkonfig {
    packageName = "com.eli.examples.dailypulse"

    defaultConfigs {
        val apiKey: String = gradleLocalProperties(rootDir, providers).getProperty("NEWS_API_KEY")

        require(apiKey.isNotEmpty()) {
            "Register your api key from developer and place it in local.properties as `NEWS_API_KEY`"
        }

        buildConfigField(FieldSpec.Type.STRING, "NEWS_API_KEY", apiKey)
    }
}

android {
    namespace = "com.eli.examples.dailypulse"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

sqldelight {
    databases {
        create(name = "DailyPulseDatabase") {
            packageName.set("com.eli.examples.dailypulse.db")
        }
    }
}