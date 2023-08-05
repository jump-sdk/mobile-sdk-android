@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    kotlin("android")
    id("com.android.library")
}

android {
    namespace = "com.spreedly.composewidgets"
    compileSdk = libs.versions.compileSdk.get().toInt()

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
    implementation(libs.androidx.core)
    implementation(libs.appcompat)
    implementation(libs.compose.material)
    api(projects.coreKmm)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
