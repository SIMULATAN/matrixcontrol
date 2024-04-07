plugins {
    id("com.android.application")
    id("com.android.library") apply false
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("matrixcontrol.android.compose")
    id("matrixcontrol.android.application")
    id("matrixcontrol.android.library") apply false
}

android {
    defaultConfig {
        applicationId = "com.github.simulatan.matrixcontrol"
        minSdk = 29
        targetSdk = 34
        versionCode = 3
        versionName = "2.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.04.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("com.github.simulatan.matrixcontrol:matrixcontrol-protocol:1.1.0")
    implementation("com.github.simulatan.matrixcontrol:matrixcontrol-relay:1.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    implementation(projects.app.relayProviderApi)
    implementation(projects.app.relayProviderLogging)
    implementation(projects.app.relayProviderKtor)
    implementation(projects.app.relayProviderTcp)
    implementation(projects.app.relayProviderDigionesp)
}
