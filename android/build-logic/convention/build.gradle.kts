import org.jetbrains.kotlin.gradle.dsl.JvmTarget

group = "com.ssafy.convention"

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin{
    plugins{
        register("androidComposeApplicationConventionPlugin"){
            id = "moya.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidComposeLibraryConventionPlugin"){
            id = "moya.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibraryConventionPlugin"){
            id = "moya.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidHiltConventionPlugin"){
            id = "moya.android.hilt"
            implementationClass = "HiltConventionPlugin"
        }
    }
}