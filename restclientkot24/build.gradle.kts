import java.io.ByteArrayOutputStream

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("maven-publish") // ✅ Required for publishing
}

android {
    namespace = "com.supunishara.restclientkot24"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
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

    // ✅ Unit Testing (JUnit 4)
    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.test.core)

    testImplementation(libs.androidx.test.runner)
    testImplementation(libs.androidx.espresso.core)

    // ✅ Android Instrumented Tests (JUnit 4)
    androidTestImplementation(libs.bundles.android.instrumented)
}

fun getGitTagVersion(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine = listOf("git", "describe", "--tags", "--abbrev=0")
        standardOutput = stdout
    }
    val tag = stdout.toString().trim().removePrefix("v")

    val dirty = ByteArrayOutputStream()
    exec {
        commandLine = listOf("git", "status", "--porcelain")
        standardOutput = dirty
    }

    return if (dirty.toString().isBlank()) tag else "$tag-dirty"
}


afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.SupunIsharaWK"
                artifactId = "restclientkot24"
                version = getGitTagVersion() // ← must be a fresh version
            }

//            create<MavenPublication>("debug") {
//                from(components["debug"])
//                groupId = "com.github.SupunIsharaWK"
//                artifactId = "restclientkot24"
//                version = "1.0.5" // ← same fresh version
//            }
        }

        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/SupunIsharaWK/AndroidRestClientKot24")
                credentials {
                    username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                    password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
                }
            }
        }
    }
}
