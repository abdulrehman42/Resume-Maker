pluginManagement {
    repositories {
        jcenter()
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://jcenter.bintray.com")
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        jcenter()
        google()
        mavenCentral()
        maven(url = "https://jcenter.bintray.com")
        maven(url = "https://jitpack.io")
        maven(url = "https://jcenter.bintray.com")
        maven(url = "https://android-sdk.is.com/")

        maven(url = "https://cboost.jfrog.io/artifactory/chartboost-ads/")

        maven(url = "https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
        maven(url = "https://artifact.bytedance.com/repository/pangle")
        maven(url = "https://s3.amazonaws.com/smaato-sdk-releases/")
        maven(
            url = "https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea"
        )
        maven(
            url = "https://artifact.bytedance.com/repository/pangle/"
        )
        maven(
            url = "https://artifact.bytedance.com/repository/pangle/"
        )
        maven(
            url = "https://artifact.bytedance.com/repository/pangle/"
        )
        maven(
            url = "https://jcenter.bintray.com/"
        )
        maven(
            url = uri("https://cboost.jfrog.io/artifactory/chartboost-mediation")
        )
        maven(url = "https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
        flatDir {
            dirs("libs")
        }
        maven(
            url = "https://cboost.jfrog.io/artifactory/chartboost-ads/"
        )
        maven {
            url = uri("https://cboost.jfrog.io/artifactory/chartboost-ads")
            name = "Chartboost's maven repo"

        }
        maven(
            url = "https://android-sdk.is.com/"
        )
//        maven {
//            name "ironSource's maven repo"
//            url "https://android-sdk.is.com/"
//        }
        maven(
            url = "https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea"
        )
        jcenter()
    }
}

rootProject.name = "Resume Maker"
include(":app")
