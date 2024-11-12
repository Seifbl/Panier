plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.panier"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.panier"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("androidx.room:room-runtime:2.2.0")
    annotationProcessor("androidx.room:room-compiler:2.2.0")
    implementation("androidx.recyclerview:recyclerview:1.3.0") // Dernière version
    implementation("androidx.cardview:cardview:1.0.0")
    implementation ("com.google.code.gson:gson:2.8.9")
    implementation ("com.stripe:stripe-android:20.11.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")


}