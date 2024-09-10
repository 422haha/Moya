import java.util.Properties

plugins {
    alias(libs.plugins.module.android.application.compose)
    alias(libs.plugins.module.hilt)
}

val properties = Properties()
properties.load(rootProject.file("local.properties").inputStream())

android {
    namespace = "com.ssafy.demo"

    defaultConfig {
        applicationId = "com.ssafy.demo"
        minSdk = 26
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }


        manifestPlaceholders["AR_KEY"] = properties["AR_KEY"] ?: ""
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":feat:ar"))
    implementation(project(":core:network"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
}