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
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.36.0")
    implementation ("com.github.a914-gowtham:compose-ratingbar:1.2.3")
    implementation(libs.coil.compose)
    implementation(libs.play.services.maps)
    implementation(project(":core:network"))
    implementation(project(":core:model"))
    implementation(project(":feat:ai"))
    implementation(libs.onnxruntime)
}