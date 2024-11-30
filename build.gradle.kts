import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
	kotlin("jvm") version "1.9.21"
	`maven-publish`
}

group = "com.github.simulatan"
version = "1.1.0"

repositories {
	mavenCentral()
}

subprojects {
	tasks.withType<KotlinCompilationTask<*>>().configureEach {
		compilerOptions.freeCompilerArgs.add("-opt-in=kotlin.ExperimentalUnsignedTypes,kotlin.ExperimentalStdlibApi")
	}
}
