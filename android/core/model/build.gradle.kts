plugins {
    alias(libs.plugins.module.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.ssafy.model"

}

dependencies {
    implementation(libs.bundles.serialization)
}