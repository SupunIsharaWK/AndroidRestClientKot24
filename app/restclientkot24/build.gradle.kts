plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.supunishara.restclientkot24"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        targetSdk = 35

        // No applicationId for library module
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

    // Unit Testing
    testImplementation(libs.junit.jupiter) // JUnit 5 (Jupiter)
    testImplementation(libs.kotlinx.coroutines.test) // Coroutines test library
    testImplementation(libs.mockito.core) // Mockito for mocking
    testImplementation(libs.mockk) // Mockk for mocking in tests
}