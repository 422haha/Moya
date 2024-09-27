plugins {
    alias(libs.plugins.module.android.library.compose)
    alias(libs.plugins.module.hilt)
}

android {
    namespace = "com.ssafy.ar"

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    implementation("io.github.sceneview:arsceneview:2.2.1")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation(libs.play.services.maps)
    implementation(project(":core:network"))
    implementation(project(":core:model"))
}