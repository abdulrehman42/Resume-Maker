plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.example.resumemaker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.pentabit.cvmaker.resumebuilder"
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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    //Navigation Component
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    //Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    //SDP
    implementation(libs.sdp.android)
    implementation(libs.glide)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.auth)
    annotationProcessor(libs.compiler)

    //SSP
    implementation(libs.ssp.android)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //Okhttp logging intercepter
    implementation(libs.logging.interceptor)

    //LifeCycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.common.java8)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.viewpager2)
    implementation(libs.material.v190)
    implementation("com.google.android.material:material:1.3.0-alpha02")


    implementation(files("libs/AppsKitSDK_v380.aar"))
    implementation("com.github.ome450901:SimpleRatingBar:1.5.1")
    implementation("io.supercharge:shimmerlayout:2.1.0")

//    implementation("com.google.android.gms:play-services-ads:22.6.0")
    implementation("com.google.android.gms:play-services-ads-lite:22.2.0")
    implementation("com.google.ads.mediation:applovin:12.5.0.0")
    implementation("com.google.ads.mediation:chartboost:9.7.0.0")
    implementation("com.google.ads.mediation:inmobi:10.6.7.1")
    implementation("com.google.ads.mediation:ironsource:8.1.0.0")
    implementation("com.google.ads.mediation:vungle:7.3.2.0")
    implementation("com.google.ads.mediation:facebook:6.17.0.0")
    implementation("com.google.ads.mediation:mintegral:16.7.41.0")
    implementation("com.google.ads.mediation:pangle:6.0.0.3.0")
    implementation("com.unity3d.ads:unity-ads:4.11.3")
    implementation("com.google.ads.mediation:unity:4.11.3.0")
    implementation("com.google.android.gms:play-services-base:18.0.1")
    implementation("com.google.android.gms:play-services-ads-identifier:18.0.1")
    implementation("com.google.android.gms:play-services-appset:16.0.2")
    // Helium Mediation
    implementation("com.chartboost:chartboost-mediation-sdk:4.7.1")
    // Chartboost Mediation SDK Dependencies
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("com.chartboost:chartboost-mediation-adapter-adcolony:4.4.8.0.4")
    implementation("com.chartboost:chartboost-mediation-adapter-admob:4.22.3.0.4")
//    implementation("com.chartboost:chartboost-mediation-adapter-amazon-publisher-services:4.9.8.6.0")
    implementation("com.chartboost:chartboost-mediation-adapter-applovin:4.12.0.0.0")
    implementation("com.chartboost:chartboost-mediation-adapter-chartboost:4.9.6.0.0")
//    implementation("com.chartboost:chartboost-mediation-adapter-digital-turbine-exchange:4.8.2.4.0")
    implementation("com.chartboost:chartboost-mediation-adapter-google-bidding:4.22.3.0.4")
    implementation("com.chartboost:chartboost-mediation-adapter-inmobi:4.10.6.2.0")
    implementation("com.chartboost:chartboost-mediation-adapter-ironsource:4.7.5.2.0.0")
    implementation("com.chartboost:chartboost-mediation-adapter-meta-audience-network:4.6.16.0.0")
    implementation("com.chartboost:chartboost-mediation-adapter-mintegral:4.16.3.91.5")
//    implementation("com.chartboost:chartboost-mediation-adapter-tapjoy:4.13.0.0.0")
    implementation("com.chartboost:chartboost-mediation-adapter-pangle:4.5.5.0.3.0")
    implementation("com.chartboost:chartboost-mediation-adapter-reference:4.1.0.1.2")
    implementation("com.chartboost:chartboost-mediation-adapter-unity-ads:4.4.9.1.0")
    implementation("com.chartboost:chartboost-mediation-adapter-vungle:4.7.1.0.0")
//    implementation("com.chartboost:chartboost-mediation-adapter-yahoo:4.1.4.0.3")
    // iron source Mediation
    implementation("com.ironsource.sdk:mediationsdk:7.7.0")
    // Add Applovin Network
    implementation("com.ironsource.adapters:applovinadapter:4.3.41")
    implementation("com.applovin:applovin-sdk:12.4.2")
    implementation("com.applovin.mediation:vungle-adapter:7.3.1.2")
// Add Chartboost Network
    implementation("com.ironsource.adapters:chartboostadapter:4.3.15")
    implementation("com.chartboost:chartboost-sdk:9.6.1")
// Add Facebook Network
    implementation("com.ironsource.adapters:facebookadapter:4.3.45")
    implementation("com.facebook.android:audience-network-sdk:6.16.0")
// Add AdMob and Ad Manager Network
//    implementation("com.google.android.gms:play-services-ads:22.6.0")
    implementation("com.ironsource.adapters:admobadapter:4.3.41")
// Add Vungle Network
    implementation("com.ironsource.adapters:vungleadapter:4.3.23")
    implementation("com.vungle:vungle-ads:7.1.0")
    implementation("com.ironsource.adapters:mintegraladapter:4.3.23")
//overseas market
    implementation("com.mbridge.msdk.oversea:mbbid:16.6.21")
    implementation("com.mbridge.msdk.oversea:reward:16.6.21")
    implementation("com.mbridge.msdk.oversea:mbbanner:16.6.21")
    implementation("com.mbridge.msdk.oversea:newinterstitial:16.6.21")
// Add Pangle Network
    implementation("com.ironsource.adapters:pangleadapter:4.3.24")
    implementation("com.pangle.global:ads-sdk:5.6.0.4")
// Add Smaato Network
    implementation("com.ironsource.adapters:smaatoadapter:4.3.9")
    implementation("com.smaato.android.sdk:smaato-sdk-banner:22.0.2")
    implementation("com.smaato.android.sdk:smaato-sdk-in-app-bidding:22.0.2")
// Add UnityAds Network
    implementation("com.ironsource.adapters:unityadsadapter:4.3.34")
    implementation("com.unity3d.ads:unity-ads:4.9.2")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("com.google.android.gms:play-services-appset:16.0.0")
//    implementation("com.google.android.gms:play-services-basement:18.1.0")
    // Retrofit
    val retrofit_version = "2.9.0"

    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    //for max pangle adapter
    implementation("com.applovin.mediation:bytedance-adapter:+")
    implementation("com.applovin.mediation:chartboost-adapter:+")
    implementation("com.google.android.gms:play-services-base:16.1.0")
    implementation("com.applovin.mediation:google-ad-manager-adapter:+")
    implementation("com.applovin.mediation:inmobi-adapter:+")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("com.applovin.mediation:ironsource-adapter:+")
    implementation("com.applovin.mediation:vungle-adapter:+")
    implementation("com.applovin.mediation:facebook-adapter:+")
    implementation("com.applovin.mediation:mintegral-adapter:+")
    implementation("com.applovin.mediation:unityads-adapter:+")
    // AppsFlyer
    implementation("com.appsflyer:af-android-sdk:6.13.0")
    implementation("com.appsflyer:adrevenue:6.9.0")
    implementation("com.miui.referrer:homereferrer:1.0.0.6")
    //Adjust
    implementation("com.adjust.sdk:adjust-android:4.38.3")
    implementation("com.android.installreferrer:installreferrer:2.2")
    implementation("com.google.android.gms:play-services-ads-identifier:18.0.1")
    implementation("com.google.android.gms:play-services-appset:16.0.2")

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.android.gms:play-services-measurement")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-config")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.google.android.gms:play-services-auth:20.6.0")



    /*//KProgressHud
    implementation ("com.kaopiz:kprogresshud:1.2.0")
*/
    //hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
kapt {
    correctErrorTypes = true
}
