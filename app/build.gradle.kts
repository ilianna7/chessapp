plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")  // Apply the kapt plugin for annotation processing
}

android {
    namespace = "com.example.chessapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.chessapp"
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

    buildFeatures {
        dataBinding = true  // Enable Data Binding
    }
    buildFeatures {
        viewBinding = true  // Enable View Binding
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Default Dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Other dependencies
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation("androidx.fragment:fragment-ktx:1.7.0")

    //Fragment Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

    // SplashScreen
    implementation("androidx.core:core-splashscreen:1.0.0")

    // Example for other libraries
    // implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // implementation("androidx.room:room-runtime:2.5.0")
    // kapt("androidx.room:room-compiler:2.5.0")
}
