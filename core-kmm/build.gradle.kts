object Versions {
    const val ktor = "2.3.2"
    const val kotlin = "1.8.22"
}

plugins {
    kotlin("multiplatform") version "1.8.22"
    kotlin("plugin.serialization") version "1.8.22"
    id("com.android.library")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
@Suppress("UnusedPrivateProperty")
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "spreedly-core"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:${Versions.ktor}")
                implementation("io.ktor:ktor-client-content-negotiation:${Versions.ktor}")
                implementation("io.ktor:ktor-client-logging:${Versions.ktor}")
                implementation("io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:${Versions.ktor}")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:${Versions.ktor}")
            }
        }
    }
}

android {
    namespace = "com.spreedly.client"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}
