plugins {
    kotlin("multiplatform") version libs.versions.kotlin.get() apply false
    kotlin("plugin.serialization") version libs.versions.kotlin.get() apply false
    id("org.jetbrains.kotlin.android") version libs.versions.kotlin.get() apply false
    id("com.android.library") version libs.versions.android.gradle.plugin.get() apply false
    id("com.android.application") version libs.versions.android.gradle.plugin apply false
    id("io.gitlab.arturbosch.detekt") version libs.versions.detekt.get()
}

allprojects {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    apply(plugin = "io.gitlab.arturbosch.detekt")
    detekt {
        config.setFrom(files(project.rootProject.file("detekt.yml")))
        parallel = true
        reports {
            xml.required.set(true)
            txt.required.set(false)
            html.required.set(false)
            sarif.required.set(false)
            md.required.set(false)
        }
    }
    dependencies {
        val detektVersion = rootProject.libs.versions.detekt.get()
        detektPlugins("ru.kode:detekt-rules-compose:1.3.0")
        detektPlugins("io.nlopez.compose.rules:detekt:0.1.12")
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-rules-libraries:$detektVersion")
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")
        detektPlugins("com.braisgabin.detekt:kotlin-compiler-wrapper:0.0.4")
    }
}
