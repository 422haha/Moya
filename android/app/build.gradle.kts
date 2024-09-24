import java.util.Properties

plugins {
    alias(libs.plugins.module.android.application.compose)
    alias(libs.plugins.module.hilt)
    alias(libs.plugins.kotlin.serialization)
}

val properties = Properties()
properties.load(rootProject.file("local.properties").inputStream())

android {
    namespace = "com.ssafy.moya"

    defaultConfig {
        applicationId = "com.ssafy.moya"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("Boolean", "DEBUG", properties["DEBUG"] as String)
        buildConfigField("String", "NAVER_CLIENT_ID", "\"${properties["NAVER_CLIENT_ID"]}\"")
        manifestPlaceholders["NAVER_CLIENT_ID"] = properties["NAVER_CLIENT_ID"] as String
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
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

    //compose-navigation
    implementation(libs.androidx.navigation.compose)

    //serialization
    implementation(libs.kotlinx.serialization.core)

    implementation(project(":feat:ar"))
    implementation("io.github.sceneview:arsceneview:2.2.1")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    implementation(project(":feat:main"))
    implementation(project(":core:ui"))
}