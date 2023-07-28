allprojects {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
//    id("com.android.tools.build:gradle") version libs.versions.android.gradle.plugin.get()
    kotlin("multiplatform") version libs.versions.kotlin.get() apply false
    kotlin("plugin.serialization") version libs.versions.kotlin.get() apply false
    id("org.jetbrains.kotlin.android") version libs.versions.kotlin.get() apply false
    id("com.android.library") version libs.versions.android.gradle.plugin.get() apply false
    id("com.android.application") version libs.versions.android.gradle.plugin apply false
}
