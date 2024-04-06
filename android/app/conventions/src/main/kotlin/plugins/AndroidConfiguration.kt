package plugins

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

fun PluginManager.baseAndroidPlugins() {
	apply("org.jetbrains.kotlin.android")
}

fun CommonExtension<*, *, *, *, *, *>.baseAndroidConfiguration() {
	namespace = "com.github.simulatan"
	compileSdk = 34
	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.11"
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}

	(this as ExtensionAware).configure<KotlinJvmOptions> {
		jvmTarget = "1.8"
	}
}

fun DependencyHandler.addComposeDependencies() {
	add("api", "androidx.core:core-ktx:1.12.0")
	add("api", "androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
	add("api", "androidx.activity:activity-compose:1.8.2")
	add("api", platform("androidx.compose:compose-bom:2024.04.00"))
	add("api", "androidx.compose.ui:ui")
	add("api", "androidx.compose.ui:ui-graphics")
	add("api", "androidx.compose.ui:ui-tooling-preview")
	add("api", "androidx.compose.material3:material3")
	add("api", "androidx.datastore:datastore-preferences:1.1.0-beta02")
	add("api", "androidx.navigation:navigation-compose:2.7.7")
}
