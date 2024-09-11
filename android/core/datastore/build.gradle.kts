plugins {
    alias(libs.plugins.module.android.library)
    alias(libs.plugins.module.hilt)
}

android {
    namespace = "com.ssafy.datastore"
}

dependencies {
    implementation(libs.datastore.preferences)
}
