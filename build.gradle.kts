import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

plugins {
	kotlin("jvm") version "2.1.0" apply false
	kotlin("multiplatform") version "2.1.0" apply false
	kotlin("plugin.serialization") version "2.1.0" apply false
}

group = "com.github.simulatan"
version = "1.1.0"

repositories {
	mavenCentral()
}

subprojects {
	afterEvaluate {
		kotlinExtension.sourceSets.all {
			languageSettings.optIn("kotlin.ExperimentalUnsignedTypes")
			languageSettings.optIn("kotlin.ExperimentalStdlibApi")
		}
	}
}
