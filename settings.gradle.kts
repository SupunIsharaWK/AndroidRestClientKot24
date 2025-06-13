pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    this.repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/SupunIsharaWK/AndroidRestClientKot24")
            credentials {
                username = providers.gradleProperty("gpr.user").orElse(System.getenv("USERNAME")).get()
                password = providers.gradleProperty("gpr.key").orElse(System.getenv("TOKEN")).get()
            }
        }
    }
}

rootProject.name = "RestClientKot24"
include(":app", ":restclientkot24")

