plugins {
    alias(libs.plugins.module.android.library)
    alias(libs.plugins.module.hilt)
}

android {
    namespace = "com.skele.moya.background"
}

dependencies {
    implementation(libs.play.services.location)
    implementation(project(":core:model"))
}