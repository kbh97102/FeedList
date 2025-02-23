plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
//    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlinx.serialization.json)
    alias(libs.plugins.secret)
}

android {
    namespace = "com.arakene.presentation"
    compileSdk = 35

    defaultConfig{
        minSdk = 28
        targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    apply(from = "../common.gradle")

    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(project(":domain"))

    //    add
    implementation(libs.exoplayer)
    implementation(libs.exoplayer.ui)
    implementation(libs.exoplayer.common)
//    implementation(libs.hilt)
//    implementation(libs.hilt.navigation)
//    kapt(libs.hilt.compiler)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.compose.navigation)
    implementation(libs.secret)
    implementation(libs.androidx.material3.android)
}