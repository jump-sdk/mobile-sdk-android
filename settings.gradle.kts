pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/")
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":core-kmm")
include(":sdk_sample")
include(":securewidgets")
include(":securewidgets-compose")
