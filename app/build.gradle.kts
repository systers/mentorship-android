plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-android-extensions")
}
android {

    compileSdk = Versions.compileSdkVersion
    defaultConfig {
        applicationId = "org.anitab.mentorship"
        minSdk = Versions.minSdkVersion
        targetSdk = Versions.targetSdkVersion
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }
    buildFeatures {
        dataBinding = true
        // Enables Jetpack Compose for this module
        compose = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            manifestPlaceholders["usesCleartextTraffic"] = false
        }
        getByName("debug") {
            manifestPlaceholders["usesCleartextTraffic"] = true
        }
//        create("debug_localhost") {
//            initWith(getByName("debug"))
//        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.0-beta07"
    }
    androidExtensions {
        isExperimental = true
    }
}

configurations.all {
    resolutionStrategy {
        this.setForcedModules(setOf(Dependencies.support_annotation))
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Dependencies.kotlin_stdlib)
    implementation(Dependencies.design)
    implementation(Dependencies.constraint_layout)
    implementation(Dependencies.appCompat)
    testImplementation(Dependencies.junit)
    androidTestImplementation(Dependencies.test_runner)
    androidTestImplementation(Dependencies.test_rules)
    androidTestImplementation(Dependencies.espresso)
    androidTestImplementation(Dependencies.espresso_intents)
    androidTestImplementation(Dependencies.ext_junit)
    implementation(Dependencies.espresso_idling_resources)

    implementation(Dependencies.rx_java)
    implementation(Dependencies.rx_android)
    implementation(Dependencies.rx_kotlin)

    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofit_gson_converter)
    implementation(Dependencies.retrofit_rxjava2_adapter)
    implementation(Dependencies.okhttp3_logging_interceptor)

    implementation(Dependencies.lifecycle_viewmodel)
    implementation(Dependencies.app_intro)
    implementation(Dependencies.circule_image_view)

    implementation(Dependencies.fragment_ktx)

    implementation(Dependencies.viewPager2)
    implementation(Dependencies.swipe_refresh_layout)

    // Compose Dependencies
    implementation(Dependencies.Compose.UI)
    // Tooling support (Previews, etc.)
    implementation(Dependencies.Compose.Tooling)
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation(Dependencies.Compose.Foundation)
    // Material Design
    implementation(Dependencies.Compose.Material)
    // Material design icons
    implementation(Dependencies.Compose.Icons)
    implementation(Dependencies.Compose.IconsExtended)
    // Integration with observables
    implementation(Dependencies.Compose.LiveData)
    implementation(Dependencies.Compose.Activity)
    // UI Tests
    androidTestImplementation(Dependencies.Compose.Test)
}
