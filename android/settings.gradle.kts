pluginManagement {
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
        mavenLocal()
    }
}

rootProject.name = "matrixcontrol"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":app:relay-provider-api")
include(":app:relay-provider-logging")
include(":app:relay-provider-ktor")
include(":app:relay-provider-tcp")
include(":app:relay-provider-digionesp")
includeBuild("app/conventions")
