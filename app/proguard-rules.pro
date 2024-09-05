# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

    -keepattributes Signature
    -keepattributes Annotation

    -keep class com.google.android.gms.common.GooglePlayServicesUtil {*;}
    -keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {*;}
    -keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {*;}

    # Support for Google Play Services
    # http://developer.android.com/google/play-services/setup.html

    -keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final * NULL;
    }

    -keepnames @com.google.android.gms.common.annotation.KeepName class *
    -keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
    }

    -keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
    }

    # For AdMob
    -keep class com.google.android.gms.ads.** { *; }
    -dontwarn com.google.android.gms.ads.**

    # For mediation adapters
    -keep class com.google.android.gms.ads.mediation.** { *; }
    -dontwarn com.google.android.gms.ads.mediation.**

    -keep class com.facebook.** { *; }
    -keep class com.facebook.imagepipeline.image.EncodedImage { *; }
    -keep public class com.facebook.imagAppsKitSDKUtils.** {
    public *;
    }

    # Preserve AdMob Mediation libraries
    -keep class com.google.ads.mediation.unity.** { *; }
    -keep class com.google.ads.mediation.vungle.** { *; }
    -keep class com.google.ads.mediation.facebook.** { *; }
    -keep class com.google.ads.mediation.applovin.** { *; }
    -keep class com.bytedance.sdk.openadsdk.** { *; }
    # Your other mediation adapter classes for adMob mediation

    -keep public class com.google.android.gms.ads.** {
    public *;
    }

    -keep public class com.google.ads.** {
    public *;
    }

    -keep class com.chartboost.heliumsdk.** { *; }

    -keep class com.chartboost.mediation.** { *; }

    -keep,includedescriptorclasses class kotlinx.serialization.** {
    *;
    }

    -keep,includedescriptorclasses @kotlinx.serialization.Serializable class * {
    *;
    }

    -keepclassmembers class kotlinx.serialization.internal.** {
    *;
    }

    # For Google Mobile Ads SDK and Firebase SDK
    -keep class com.google.android.gms.** { *; }
    -keep class com.google.firebase.** { *; }
    -dontwarn com.google.android.gms.**
    -dontwarn com.google.firebase.**

    #for Gson
    -keep class com.google.gson.** { *; }
    -keep class com.google.gson.reflect.TypeToken
    -keep class * extends com.google.gson.reflect.TypeToken
    -keep public class * implements java.lang.reflect.Type

    #Retrofit
    -dontwarn retrofit2.**
    -dontwarn com.squareup.okhttp3.**
    -keep class retrofit2.** { *; }
    -keep interface retrofit2.** { *; }
    -keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
    }

    -keepclassmembers class ** {
    @retrofit2.http.* <methods>;
    }

    # OkHttp3
    -dontwarn okhttp3.**
    -dontwarn okio.**
    -dontwarn javax.annotation.**
    -dontwarn org.conscrypt.**
    -keep class okhttp3.** { *; }
    -keep interface okhttp3.** { *; }
    -keep class okhttp3.internal.** { *; }
    -keep class okhttp3.internal.http2.** { *; }
