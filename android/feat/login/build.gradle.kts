import java.util.Properties

plugins {
    alias(libs.plugins.module.android.library.compose)
    alias(libs.plugins.module.hilt)
}

val properties = Properties()
properties.load(rootProject.file("local.properties").inputStream())

android {
    namespace = "com.ssafy.moya.login"

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    defaultConfig {
        buildConfigField("String", "OAUTH_CLIENT_ID", properties["OAUTH_CLIENT_ID"] as String)
        buildConfigField(
            "String",
            "OAUTH_CLIENT_SECRET",
            properties["OAUTH_CLIENT_SECRET"] as String,
        )
        buildConfigField("String", "OAUTH_CLIENT_NAME", properties["OAUTH_CLIENT_NAME"] as String)
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:network"))
    implementation(project(":core:model"))

    //implementation(files("libs/oauth-5.10.0.aar"))
    implementation(libs.oauth)

    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.legacy.support.core.utils)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.security.crypto)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.moshi.kotlin)
    implementation(libs.logging.interceptor.v421)
    implementation(libs.lottie)
}
