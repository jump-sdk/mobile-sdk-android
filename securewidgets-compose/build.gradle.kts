@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    kotlin("android")
    id("com.android.library")
    id("maven-publish")
    id("org.jetbrains.dokka")
}

group = "com.jump.spreedly"
version = System.getenv()["GITHUB_RUN_NUMBER"] ?: "1"

android {
    namespace = "com.spreedly.composewidgets"
    compileSdk = libs.versions.compileSdk.get().toInt()
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    lint {
        checkDependencies = true
        abortOnError = true
        checkDependencies = true
        ignoreWarnings = false
        checkAllWarnings = true
        warningsAsErrors = true
        explainIssues = true
        showAll = true
        disable.add("InvalidPackage")
        xmlReport = true
        htmlReport = false
        baseline = file("lint-baseline.xml")
    }
}

dependencies {
    api(projects.coreKmm)
    implementation(libs.androidx.core)
    implementation(libs.appcompat)
    implementation(libs.compose.activity)
    implementation(libs.compose.material)
    implementation(libs.play.services.wallet)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

publishing {
    publications {
        create<MavenPublication>("release") {
            afterEvaluate {
                tasks.withType(AbstractPublishToMaven::class.java) {
                    dependsOn(tasks.getByName("assembleRelease"))
                }
                from(components["release"])
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/jump-sdk/mobile-sdk-android")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
