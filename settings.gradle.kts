enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "ToDometerMultiplatform"

include(":android")
include(":common:android")
include(":common:core")
include(":common:compose-ui")
include(":common:compose-ui-designsystem")
include(":common:data")
include(":common:database")
include(":common:domain")
include(":common:navigation")
include(":common:network")
include(":common:preferences")
include(":common:resources")
include(":common:ui")
include(":desktop")
include(":ios-compose-ui")
include(":wearos")
