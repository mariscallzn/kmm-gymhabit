pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "GymHabit"
include(":androidApp")
include(":shared")
enableFeaturePreview("VERSION_CATALOGS")