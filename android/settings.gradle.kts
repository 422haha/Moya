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
    }
}

rootProject.name = "Moya"
include(":app")
include(":core:network")
include(":core")
include(":core:model")
include(":core:ui")
include(":core:data")
include(":feat")
include(":feat:ar")
include(":demo")
include(":core:datastore")
