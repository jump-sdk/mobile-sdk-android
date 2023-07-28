pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/")
    }
}

include(":core-sdk")
include(":sdk_sample")
include(":securewidgets")
include(":core-kmm")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
