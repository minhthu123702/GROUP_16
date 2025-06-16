plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.hanoi_local_hub"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.hanoi_local_hub"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)// Google Sign-In (for authentication with Google accounts)
    implementation("com.google.android.gms:play-services-auth:21.1.1")

    // (Optional) For using Firebase UI Auth (pre-built UI for authentication)
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")
    androidTestImplementation(libs.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-auth")
    // Firebase Bill of Materials (BoM) - Quản lý phiên bản các thư viện Firebase

}