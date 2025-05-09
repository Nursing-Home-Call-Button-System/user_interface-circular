plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    }
apply(plugin = "com.google.gms.google-services")

android {
    namespace = "com.example.navbar"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.navbar"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.play.services.wearable)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.compose.material)
    implementation(libs.compose.foundation)
    implementation(libs.wear.tooling.preview)
    implementation(libs.activity.compose)
    implementation(libs.core.splashscreen)
    implementation(libs.compose.navigation)
    implementation(libs.navigation.compose)
    implementation(libs.material3.android)
    implementation(libs.material.icons.core.android)

    // Firebase
    implementation(platform(libs.firebase.bom)) // Ensure latest BOM version
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth.ktx)
    implementation("com.google.firebase:firebase-storage-ktx")

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}
