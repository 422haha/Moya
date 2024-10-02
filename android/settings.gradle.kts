pluginManagement {
    includeBuild("build-logic")
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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://repository.map.naver.com/archive/maven")
        }
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "Moya"
include(":app")
include(":core:network")
include(":core")
include(":core:ui")
include(":feat")
include(":feat:ar")
include(":demo")
include(":core:datastore")
include(":feat:main")
include(":core:model")
include(":feat:ai")
