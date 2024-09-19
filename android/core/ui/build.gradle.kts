plugins {
    alias(libs.plugins.module.android.library.compose)
}

android {
    namespace = "com.ssafy.ui"

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    implementation(libs.naver.map.compose)
    implementation(libs.play.services.location)
    implementation(libs.toolbar.compose)
}