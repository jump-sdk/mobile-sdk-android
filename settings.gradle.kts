pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://oss.sonatype.org/content/repositories/snapshots/") {
            content {
                includeGroup("app.cash.paparazzi")
            }
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":core-kmm")
include(":sdk_sample")
include(":securewidgets-compose")
include(":domain")
