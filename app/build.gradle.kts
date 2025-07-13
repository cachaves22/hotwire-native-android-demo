plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("org.jetbrains.kotlin.plugin.serialization")

    // Required for Notification Token Component.
    id("com.google.gms.google-services")
}

android {
    namespace = "com.camilasouza.sampleapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.camilasouza.sampleapp"
        minSdk = 28
        targetSdk = 35
        versionCode = 4
        versionName = "4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
           buildConfigField(
               type = "String",
               name = "BASE_URL",
               value = "\"http://10.0.2.2:3000\""
           )
        }
        release {
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = "\"https://app.sampleapp.org\""
            )
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.hotwire.core)
    implementation(libs.hotwire.navigation.fragments)
    implementation(libs.kotlinx.serialization.json)
    implementation(platform(libs.androidx.compose.bom)) // Required for Button Component, Form Component, and Menu Component.
    implementation(libs.androidx.material3)
    implementation(libs.review.ktx) // Required for Review Prompt Component.
    implementation(libs.play.services.location) // Required for Location Component.
    implementation(platform(libs.firebase.bom)) // Required for Notification Token Component.
    implementation(libs.firebase.messaging) // Required for Notification Token Component.
    implementation(libs.play.services.code.scanner) // Required for Barcode Scanner Component.
    debugImplementation(libs.androidx.ui.tooling)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}