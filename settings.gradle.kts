pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven ( url= "https://jitpack.io")
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven ( url= "https://jitpack.io")
        jcenter()
    }
}

rootProject.name = "Resume Maker"
include(":app")
