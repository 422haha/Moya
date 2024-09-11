import java.util.Properties

plugins {
    alias(libs.plugins.module.android.library)
    alias(libs.plugins.module.hilt)
}

val properties = Properties()
properties.load(rootProject.file("local.properties").inputStream())

android {
    namespace = "com.ssafy.network"

    defaultConfig {
        buildConfigField("String", "BASE_URL", properties["BASE_URL"] as String)
        buildConfigField("String", "API_VERSION", properties["API_VERSION"] as String)
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.bundles.network)
}