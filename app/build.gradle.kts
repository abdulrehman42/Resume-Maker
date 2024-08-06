plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    /*id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")*/
}

android {
    namespace = "com.example.resumemaker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.resumemaker"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }


    buildFeatures {
        viewBinding=true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    //Navigation Component
    implementation (libs.navigation.fragment.ktx)
    implementation (libs.navigation.ui.ktx)

    //Coroutines
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)
    //SDP
    implementation (libs.sdp.android)
    implementation (libs.glide)
    implementation (libs.androidx.recyclerview)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    annotationProcessor (libs.compiler)

    //SSP
    implementation (libs.ssp.android)

    //Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    //Okhttp logging intercepter
    implementation (libs.logging.interceptor)

    //LifeCycle
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.androidx.lifecycle.common.java8)
    implementation (libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.viewpager2)
    implementation (libs.material.v190)


    /*//hilt
    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)*/

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
/*
kapt {
    correctErrorTypes = true
}*/
