// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt.plugin) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlinx.serialization.json) apply false
    alias(libs.plugins.secret) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp) apply false
}