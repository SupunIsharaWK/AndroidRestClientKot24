import java.io.ByteArrayOutputStream

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("maven-publish") // ✅ Required for publishing
    id("jacoco") // ✅ Required for test coverage report
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

    testOptions {
        unitTests.all {
            it.useJUnit()
            it.extensions.configure<JacocoTaskExtension> {
                // Required for Robolectric support
                isIncludeNoLocationClasses = true
            }
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

jacoco {
    toolVersion = "0.8.11" // ✅ Define here
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
    // ✅ Unit Testing (mockito)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockito.inline)
    // ✅ Unit Testing (androidx and espresso)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.runner)
    testImplementation(libs.androidx.espresso.core)

    // ✅ Android Instrumented Tests (JUnit 4)
    androidTestImplementation(libs.bundles.android.instrumented)

    testImplementation (libs.robolectric) // or latest version

    // Kotlin test helpers (optional)
    testImplementation(kotlin("test"))
    testImplementation(libs.mockk)
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

    return if (dirty.toString().isBlank()) tag else tag
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

            /**
             * this must be enabled when it is required to publish debug version,
             * until then keep it commented
             */
            /*create<MavenPublication>("debug") {
                from(components["debug"])
                groupId = "com.github.SupunIsharaWK"
                artifactId = "restclientkot24"
                version = "1.0.5" // ← same fresh version
            }*/
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

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    classDirectories.setFrom(
        fileTree("${buildDir}/intermediates/javac/debug") {
            include("**/com/supunishara/**")
            exclude("**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*")
        }
    )
    sourceDirectories.setFrom(files("src/main/java"))
    executionData.setFrom(fileTree(buildDir) {
        include("jacoco/testDebugUnitTest.exec")
    })
}

tasks.withType<Test>().configureEach {
    jvmArgs("--add-opens=java.base/jdk.internal.reflect=ALL-UNNAMED")

    // Enable JaCoCo
    extensions.configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

