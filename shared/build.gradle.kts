import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.buildkonfig.gradle.plugin)
        classpath(libs.kotlinx.serialization.json)
    }
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("com.codingfeline.buildkonfig") version "+"
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqlDelight)    
    alias(libs.plugins.kmpNativeCoroutines)
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                }
            }
        }
    }

    sourceSets.all {
        languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
        languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
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
            implementation(libs.kmp.nativecoroutines.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.sql.coroutines.extensions)
            api(libs.kmp.observableviewmodel.core)
        }

        androidMain.dependencies {
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

sqldelight {
    databases {
        create(name = "DailyPulseDatabase") {
            packageName.set("com.eli.examples.dailypulse.db")
        }
    }
}