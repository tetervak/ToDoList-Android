// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.google.hilt) apply false
    alias(libs.plugins.google.services) apply false

    //id("com.android.library") version "8.7.2" apply false

    //id("com.google.devtools.ksp") version "1.9.0-1.0.11" apply false

    id("com.google.firebase.crashlytics") version "3.0.2" apply false
    id("com.google.firebase.firebase-perf") version "1.4.2" apply false
}