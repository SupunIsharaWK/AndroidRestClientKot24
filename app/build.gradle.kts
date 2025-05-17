plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.supunishara.restclientkot24"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.supunishara.restclientkot24"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // ✅ Correct for JUnit 4 Android testing
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildToolsVersion = "35.0.0"
    ndkVersion = "27.1.12297006"
}

dependencies {
    // Core and UI libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Networking
    implementation(libs.okhttp)
    implementation(libs.core.ktx)

    // ✅ Unit Testing (JUnit 4)
    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.test.runner)
    testImplementation(libs.androidx.espresso.core)

    // ✅ Android Instrumented Tests (JUnit 4)
    androidTestImplementation(libs.bundles.android.instrumented)
}
