plugins {
	`kotlin-dsl`
}

dependencies {
	implementation("com.android.library:com.android.library.gradle.plugin:8.3.1")
	implementation("org.jetbrains.kotlin.android:org.jetbrains.kotlin.android.gradle.plugin:1.9.0")
}

gradlePlugin {
	plugins {
		register("androidCompose") {
			id = "matrixcontrol.android.compose"
			implementationClass = "plugins.AndroidComposeConventionPlugin"
		}
		register("androidLib") {
			id = "matrixcontrol.android.library"
			implementationClass = "plugins.AndroidLibraryConventionPlugin"
		}
		register("androidApp") {
			id = "matrixcontrol.android.application"
			implementationClass = "plugins.AndroidApplicationConventionPlugin"
		}
		register("relayProvider") {
			id = "matrixcontrol.relay-provider"
			implementationClass = "plugins.RelayProviderConventionPlugin"
		}
	}
}
