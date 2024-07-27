import java.util.regex.Pattern.compile

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.expensermanager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.expensermanager"
        minSdk = 24
        targetSdk = 34
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

    buildFeatures{
        viewBinding = true
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // allow viewBinding
    viewBinding {
        enable = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.blurry)

    //barchart
    implementation(libs.mpandroidchart)

    // QR-Code
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.google.zxing:core:3.3.2")
    // CameraX Core library
    implementation ("androidx.camera:camera-core:1.2.0")
    // CameraX Camera2 library
    implementation ("androidx.camera:camera-camera2:1.2.0")
    // CameraX Lifecycle library
    implementation ("androidx.camera:camera-lifecycle:1.2.0")
    // CameraX View library
    implementation ("androidx.camera:camera-view:1.2.0")
    implementation ("androidx.camera:camera-extensions:1.0.0-alpha28")
    implementation ("com.google.mlkit:barcode-scanning:17.0.2")

    // CameraX core library using the camera2 implementation
    val camerax_version = "1.4.0-beta02"
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    // CameraX Lifecycle library
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    // CameraX VideoCapture library
    implementation("androidx.camera:camera-video:${camerax_version}")
    // CameraX View class
    implementation("androidx.camera:camera-view:${camerax_version}")
    // CameraX ML Kit Vision Integration
    implementation("androidx.camera:camera-mlkit-vision:${camerax_version}")
    // CameraX Extensions library
    implementation("androidx.camera:camera-extensions:${camerax_version}")
}