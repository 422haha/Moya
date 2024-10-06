import java.util.Properties

plugins {
    alias(libs.plugins.module.android.library)
    alias(libs.plugins.module.hilt)
    alias(libs.plugins.kotlin.serialization)
}

val properties = Properties()
properties.load(rootProject.file("local.properties").inputStream())

android {
    namespace = "com.ssafy.network"

    defaultConfig {
        buildConfigField("String", "BASE_URL", properties["BASE_URL"] as String)
        buildConfigField("String", "API_VERSION", properties["API_VERSION"] as String)
        buildConfigField("String", "BUCKET_NAME", properties["BUCKET_NAME"] as String)
        buildConfigField("String", "AWS_ACCESS_KEY", properties["AWS_ACCESS_KEY"] as String)
        buildConfigField("String", "AWS_SECRET_KEY", properties["AWS_SECRET_KEY"] as String)
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.bundles.network)
    implementation(project(":core:model"))
    implementation(project(":core:datastore"))

    implementation(libs.aws.android.sdk.s3)
    implementation(libs.aws.android.sdk.core)
}
