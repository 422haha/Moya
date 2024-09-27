plugins {
    alias(libs.plugins.module.android.library.compose)
    alias(libs.plugins.module.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.ssafy.main"

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    implementation(libs.naver.map.compose)
    implementation(libs.play.services.location)
    implementation(project(":core:ui"))
    implementation(project(":core:network"))
    implementation(project(":core:model"))
}